package kr.casealot.shop.domain.order.service;


import java.util.Optional;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.file.entity.UploadFile;
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
  public APIResponse<OrderDTO.Response> createOrder(OrderDTO.createOrder createOrder,
      Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    Order order = new Order();
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
  public APIResponse<OrderDTO.Response> cancelOrder(Long orderId, Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    String customerId = principal.getName();

    // 주문내역이 존재하지않을 경우
    Order order = orderRepository.findById(orderId).orElseThrow(NotFoundOrderException::new);

    if (!order.getCustomer().getId().equals(customerId)) {
      // 본인 주문건 아닐경우
      throw new PermissionException();
    }

    if (order.getOrderStatus().equals(CANCEL)) {
      throw new OrderCanceledException();
    }

    if (order.getOrderStatus().equals(COMPLETE)) {
      throw new OrderAlreadyCompleteException();
    }

    order.setOrderStatus(CANCEL);
    orderRepository.save(order);

    OrderDTO.Response orderResponse = orderResponse(order);

    return APIResponse.success(API_NAME, orderResponse);
  }

  @Transactional
  public APIResponse<OrderDTO.Response> completeOrder(Long orderId, Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    String customerId = principal.getName();

    // 주문내역이 존재하지않을 경우
    Order order = orderRepository.findById(orderId).orElseThrow(NotFoundOrderException::new);

    // TODO 배송완료된 주문 취소불가 적용해야됨
    if (order.getOrderStatus().equals(CANCEL)) {
      throw new OrderCanceledException();
    }
    if (order.getOrderStatus().equals(COMPLETE)) {
      throw new OrderAlreadyCompleteException();
    }

    if (!order.getCustomer().getId().equals(customerId)) {
      // 본인 주문건 아닐경우
      throw new PermissionException();
    }
    if (!order.getPayment().getStatus().equals(PaymentStatus.PAID)) {
      throw new PaymentRequiredException();
    }

    //주문 완료
    order.setOrderStatus(COMPLETE);
    orderRepository.save(order);

    OrderDTO.Response orderResponse = orderResponse(order);

    return APIResponse.success(API_NAME, orderResponse);
  }

  public APIResponse<OrderDTO.Response> getOrderDetail(Long orderId, Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    Order order = orderRepository.findById(orderId).orElseThrow(NotFoundOrderException::new);

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

  public APIResponse<List<OrderDTO.Response>> getOrderCancelList(Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    List<Order> orders = orderRepository.findByCustomerAndOrderStatus(customer, CANCEL);

    List<OrderDTO.Response> orderResponses = orders.stream()
        .map(this::orderResponse)
        .collect(Collectors.toList());

    return APIResponse.success(API_NAME, orderResponses);
  }

  public APIResponse<List<OrderDTO.Response>> getOrderCompleteList(Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    List<Order> orders = orderRepository.findByCustomerAndOrderStatus(customer, COMPLETE);

    List<OrderDTO.Response> orderResponses = orders.stream()
        .map(this::orderResponse)
        .collect(Collectors.toList());

    return APIResponse.success(API_NAME, orderResponses);
  }

  public APIResponse<List<OrderDTO.Response>> getOrderChangeList(Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    List<Order> orders = orderRepository.findByCustomerAndOrderStatus(customer, CHANGE);

    List<OrderDTO.Response> orderResponses = orders.stream()
        .map(this::orderResponse)
        .collect(Collectors.toList());

    return APIResponse.success(API_NAME, orderResponses);
  }

  private OrderDTO.Response orderResponse(Order order) {
    order.getOrderProducts().size();

    List<OrderDTO.OrderProductDTO> orderProductDTOs = order.getOrderProducts()
        .stream()
        .map(orderProduct -> {
          Long productId = orderProduct.getProduct().getId();
          Optional<Product> optionalProduct = productRepository.findById(productId);

          // Optional에서 Product 엔티티를 가져옴
          Product product = optionalProduct.orElseThrow(() -> new RuntimeException("Product not found"));

          Optional<String> thumbnailUrl = Optional.ofNullable(product.getThumbnail())
              .map(UploadFile::getUrl);

          return OrderDTO.OrderProductDTO.builder()
              .productId(productId)
              .quantity(orderProduct.getQuantity())
              .thumbnail(thumbnailUrl.orElse(null))
              .name(orderProduct.getName())
              .price(orderProduct.getPrice())
              .build();
        })
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
