package kr.casealot.shop.domain.customer.repository;

import java.time.LocalDateTime;
import kr.casealot.shop.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findById(String id);

    Customer findByEmail(String email);

    void deleteById(String id);

    Customer findCustomerById(String id);

    boolean existsCustomerById(String customerId);
    boolean existsByEmail(String email);
    int countByModifiedDtBetween(LocalDateTime startDate, LocalDateTime endDate); // 날짜별 요청의 총 수를 조회하는 메서드


}
