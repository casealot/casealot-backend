package kr.casealot.shop.domain.order.service;


import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.order.delivery.domain.Delivery;
import kr.casealot.shop.domain.order.dto.OrderDTO;
import kr.casealot.shop.domain.order.dto.OrderStatus;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.order.entity.OrderProduct;
import kr.casealot.shop.domain.order.exception.OrderAlreadyCompleteException;
import kr.casealot.shop.domain.order.exception.OrderCanceledException;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.payment.entity.Payment;
import kr.casealot.shop.domain.payment.entity.PaymentStatus;
import kr.casealot.shop.domain.payment.exception.PaymentRequiredException;
import kr.casealot.shop.domain.payment.repository.PaymentRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.domain.order.exception.NotFoundOrderException;
import kr.casealot.shop.global.exception.NotFoundProductException;
import kr.casealot.shop.domain.order.exception.OrderCancelException;
import kr.casealot.shop.global.exception.PermissionException;
import kr.casealot.shop.global.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.casealot.shop.domain.order.dto.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final String API_NAME = "order";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public APIResponse<OrderDTO.Response> createOrder(OrderDTO.createOrder createOrder, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        Order order =  new Order();
        order.setCustomer(customer);
        // 배송, 결제 정보

        order.setOrderNumber(StringUtil.generateOrderNumber());
        order.setOrderDt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);

        for (OrderDTO.OrderProductDTO productDTO : createOrder.getOrderProducts()) {
            Product product = productRepository.findById(productDTO.getProductId())
                    .orElseThrow(NotFoundProductException::new);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setName(product.getName());
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
    public APIResponse<OrderDTO.Response> cancelOrder(String orderId, Principal principal){
        Customer customer = customerRepository.findById(principal.getName());
        String customerId = principal.getName();

        // 주문내역이 존재하지않을 경우
        Order order = orderRepository.findByOrderNumber(orderId);

        // 이미 취소된 주문, 배송중인 주문 취소불가
        // TODO 배송완료된 주문 취소불가 적용해야됨
        if(!order.getOrderStatus().equals(ORDER)){
            throw new OrderCancelException();
        }

        if(!order.getCustomer().getId().equals(customerId)){
            // 본인 주문건 아닐경우
            throw new PermissionException();
        }

        order.setOrderStatus(CANCEL);
        orderRepository.save(order);

        OrderDTO.Response orderResponse = orderResponse(order);

        return APIResponse.success(API_NAME, orderResponse);
    }

    @Transactional
    public APIResponse<OrderDTO.Response> completeOrder(String orderId, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        String customerId = principal.getName();

        // 주문내역이 존재하지않을 경우
        Order order = orderRepository.findByOrderNumber(orderId);

        // TODO 배송완료된 주문 취소불가 적용해야됨
        if(order.getOrderStatus().equals(CANCEL)){
            throw new OrderCanceledException();
        }
        if(order.getOrderStatus().equals(COMPLETE)){
            throw new OrderAlreadyCompleteException();
        }

        if(!order.getCustomer().getId().equals(customerId)){
            // 본인 주문건 아닐경우
            throw new PermissionException();
        }
        if(!order.getPayment().getStatus().equals(PaymentStatus.PAID)){
            throw new PaymentRequiredException();
        }


       //주문 완료
        order.setOrderStatus(COMPLETE);
        orderRepository.save(order);

        OrderDTO.Response orderResponse = orderResponse(order);

        return APIResponse.success(API_NAME, orderResponse);
    }

    public APIResponse<OrderDTO.Response> getOrderDetail(String orderId, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        Order order = orderRepository.findByOrderNumber(orderId);

        if (!order.getCustomer().getId().equals(customer.getId())) {
            // 본인 주문건이 아닌 경우
            throw new PermissionException();
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
                        .quantity(orderProduct.getQuantity())
                        .name(orderProduct.getName())
                        .price(orderProduct.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.Response.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDt(order.getOrderDt())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .customerId(order.getCustomer().getId())
                .name(order.getCustomer().getName())
                .phoneNumber(order.getCustomer().getPhoneNumber())
                .email(order.getCustomer().getEmail())
                .address(order.getCustomer().getAddress())
                .addressDetail(order.getCustomer().getAddressDetail())
                .orderProducts(orderProductDTOs)
                .build();
    }
}
