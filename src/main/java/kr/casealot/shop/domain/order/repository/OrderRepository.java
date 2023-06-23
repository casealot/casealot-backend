package kr.casealot.shop.domain.order.repository;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);

    Order findByOrderNumber(String orderId);
}
