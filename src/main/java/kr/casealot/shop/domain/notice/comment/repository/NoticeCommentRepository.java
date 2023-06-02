package kr.casealot.shop.domain.notice.comment.repository;

import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
}
