package kr.casealot.shop.domain.qna.repository;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import kr.casealot.shop.domain.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
  List<Qna> findByModifiedDt(LocalDateTime modifiedDt);
  int countByModifiedDt(LocalDateTime date); // 날짜별 요청의 총 수를 조회하는 메서드
  int countByModifiedDtBetween(LocalDateTime startDate, LocalDateTime endDate); // 날짜별 요청의 총 수를 조회하는 메서드
  int countByModifiedDtBetweenAndQnaCommentListIsNull(LocalDateTime startDate, LocalDateTime endDate); // 날짜별 요청의 총 수를 조회하는 메서드
  List<Qna> findAllByOrderByModifiedDtDesc(); // 날짜별 요청의 총 수를 조회하는 메서드
}
