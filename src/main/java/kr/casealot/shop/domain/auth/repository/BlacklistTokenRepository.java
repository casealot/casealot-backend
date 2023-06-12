package kr.casealot.shop.domain.auth.repository;

import kr.casealot.shop.domain.auth.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    BlacklistToken findByBlacklistToken(String token);
}
