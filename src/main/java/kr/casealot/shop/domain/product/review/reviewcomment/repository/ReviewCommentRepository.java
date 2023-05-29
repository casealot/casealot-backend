package kr.casealot.shop.domain.product.review.reviewcomment.repository;

import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
}
