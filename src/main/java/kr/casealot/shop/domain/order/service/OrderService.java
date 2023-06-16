package kr.casealot.shop.domain.order.service;


import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.order.dto.OrderDTO;
import kr.casealot.shop.domain.order.dto.OrderStatus;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.order.entity.OrderProduct;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.NotFoundProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kr.casealot.shop.domain.order.dto.OrderStatus.CANCEL;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final String API_NAME = "order";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public APIResponse<OrderDTO.Response> createOrder(OrderDTO.Request request, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        Order order =  new Order();
        order.setOrderNumber(request.getOrderNumber());
        order.setCustomer(customer);
        // 배송정보

        order.setOrderDt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);

        for (OrderDTO.OrderProductDTO productDTO : request.getOrderProducts()) {
            Product product = productRepository.findById(productDTO.getProductId())
                    .orElseThrow(NotFoundProductException::new);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setPrice(product.getPrice());
            orderProduct.setQuantity(productDTO.getQuantity());

            order.addOrderProduct(orderProduct);
        }

        order.calculateTotalAmount();

        orderRepository.save(order);

        OrderDTO.Response orderResponse = orderResponse(order);

        return APIResponse.success(API_NAME, orderResponse);
    }

    @Transactional
    public APIResponse<Void> cancelOrder(Long orderId, Principal principal){
        Customer customer = customerRepository.findById(principal.getName());

        // 주문내역이 존재하지않을 경우
        Order order = orderRepository.findById(orderId).orElseThrow();

        // 이미 취소된 주문, 배송중인 주문, 배송완료된 주문 취소불가

        if(!order.getCustomer().getId().equals(customer.getName())){
            // 본인 주문건 아닐경우
        }

        order.setOrderStatus(CANCEL);
        orderRepository.save(order);

        return APIResponse.success(API_NAME, null);
    }

    public APIResponse<OrderDTO.Response> getOrderDetail(Long orderId, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        Order order = orderRepository.findById(orderId).orElseThrow();

        if (!order.getCustomer().getId().equals(customer.getId())) {
            // 본인 주문건이 아닌 경우 에러 처리
        }

        OrderDTO.Response orderResponse = orderResponse(order);

        return APIResponse.success(API_NAME, orderResponse);
    }

    public APIResponse<List<OrderDTO.Response>> getOrderList(Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        List<Order> orders = orderRepository.findByCustomer(customer);

        List<OrderDTO.Response> orderResponses = orders.stream()
                .map(this::orderResponse)
                .collect(Collectors.toList());

        return APIResponse.success(API_NAME, orderResponses);
    }

    private OrderDTO.Response orderResponse(Order order) {
        List<OrderDTO.OrderProductDTO> orderProductDTOs = order.getOrderProducts()
                .stream()
                .map(orderProduct -> OrderDTO.OrderProductDTO.builder()
                        .productId(orderProduct.getProduct().getId())
                        .price(orderProduct.getPrice())
                        .quantity(orderProduct.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.Response.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDt(order.getOrderDt())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .customerId(order.getCustomer().getId())
                .orderProducts(orderProductDTOs)
                .build();
    }
}