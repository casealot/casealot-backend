package kr.casealot.shop.domain.customer.repository;

import kr.casealot.shop.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findById(String id);

    Customer findByEmail(String email);

    Long deleteById(String id);

    Long findCustomerSeqById(String id);

    Customer findCustomerById(String id);

    boolean existsCustomerById(String customerId);
    boolean existsByEmail(String email);
}
