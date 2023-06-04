package kr.casealot.shop.domain.auth.repository;

import kr.casealot.shop.domain.auth.entity.CustomerToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CustomerTokenRepository extends JpaRepository<CustomerToken, Long> {
    void deleteByCustomerSeq(Long customerSeq);
}
