package kr.casealot.shop.domain.auth.repository;

import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<CustomerRefreshToken, Long> {
    CustomerRefreshToken findById(String userId);
    CustomerRefreshToken findByIdAndRefreshToken(String userId, String refreshToken);
}
