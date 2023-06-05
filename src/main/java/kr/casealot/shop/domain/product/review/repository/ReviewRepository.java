package kr.casealot.shop.domain.product.review.repository;

import kr.casealot.shop.domain.product.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findBySeq(Long seq);
    List<Review> findByProductId(Long productId);
}
