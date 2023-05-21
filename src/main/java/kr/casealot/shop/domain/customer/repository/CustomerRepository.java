package kr.casealot.shop.domain.customer.repository;

import kr.casealot.shop.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findById(String id);

    Customer findByEmail(String email);
}
