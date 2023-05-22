package kr.casealot.shop.domain.qna.comment.repository;


import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
}
