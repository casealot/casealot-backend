package kr.casealot.shop.domain.order.repository;

import java.time.LocalDateTime;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByCustomer(Customer customer);

  Order findByOrderNumber(String orderId);

  int countByOrderDtBetween(LocalDateTime startDate, LocalDateTime endDate);

  @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDt BETWEEN :startTime AND :endTime AND o.orderStatus = 'COMPLETE'")
  long getTotalAmountBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

  @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDt BETWEEN :startTime AND :endTime AND o.orderStatus = 'CANCEL'")
  int getCanceledOrderCountBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

  @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDt BETWEEN :startTime AND :endTime AND o.orderStatus = 'CHANGE'")
  int getChangedOrderCountBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
