package kr.casealot.shop.domain.qna.comment.repository;


import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
    boolean existsByQna(Qna qna);
}
