package kr.casealot.shop.domain.product.review.repository;

import java.time.LocalDateTime;
import kr.casealot.shop.domain.product.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findBySeq(Long seq);
    int countByModifiedDtBetween(LocalDateTime startDate, LocalDateTime endDate); // 날짜별 요청의 총 수를 조회하는 메서드


}
