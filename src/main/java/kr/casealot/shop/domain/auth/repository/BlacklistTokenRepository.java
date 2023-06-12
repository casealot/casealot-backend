package kr.casealot.shop.domain.auth.repository;

import kr.casealot.shop.domain.auth.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    BlacklistToken findByBlacklistToken(String token);

    @Modifying
    @Query("DELETE FROM BlacklistToken b WHERE b.blacklistToken = :token")
    void deleteByBlacklistToken(@Param("token") String token);
}
