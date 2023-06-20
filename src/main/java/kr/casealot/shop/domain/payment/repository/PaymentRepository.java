package kr.casealot.shop.domain.payment.repository;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderIdAndCustomer(String orderId, Customer customer);
}
