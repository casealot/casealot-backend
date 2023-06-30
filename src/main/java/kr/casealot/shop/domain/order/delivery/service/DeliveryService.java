package kr.casealot.shop.domain.order.delivery.service;

import java.time.LocalDateTime;
import java.util.List;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.order.delivery.dto.DeliveryStatus;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.order.repository.OrderProductRepository;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.payment.repository.PaymentRepository;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {
  private final String API_NAME = "order";

  private final OrderRepository orderRepository;

  @Scheduled(cron = "0 0 0 * * *") // 매일 자정
  public void updateDeliveryType() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime twoDaysAgo = now.minusDays(2);

    List<Order> deliveryToUpdate = orderRepository.findAllByOrderDtBefore(twoDaysAgo);
    for (Order order : deliveryToUpdate) {
      if (order.getDeliveryStatus().equals(DeliveryStatus.READY)) {
        order.setDeliveryStatus(DeliveryStatus.DELIVERING);
      } else if (order.getDeliveryStatus().equals(DeliveryStatus.DELIVERING)) {
        LocalDateTime deliveryDate = order.getOrderDt().plusDays(2);
        if (now.isAfter(deliveryDate)) {
          order.setDeliveryStatus(DeliveryStatus.COMPLETE);
        }
      }
    }

    orderRepository.saveAll(deliveryToUpdate);
  }
}
