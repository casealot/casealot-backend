package kr.casealot.shop.domain.function.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import kr.casealot.shop.domain.function.dto.FunctionDTO;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionService {

  private final QnaRepository qnaRepository;

  public APIResponse<FunctionDTO> getFunction(LocalDateTime date) {
    LocalDateTime startDate = date.toLocalDate().atStartOfDay();
    LocalDateTime endDate = date.toLocalDate().atTime(LocalTime.MAX);
    int todaysEmptyComments = qnaRepository.countByModifiedDtBetweenAndQnaCommentListIsNull(startDate, endDate);


    FunctionDTO functionDTO = new FunctionDTO().builder()
        .todayOrder(0)
        .todayCancel(0)
        .todayReturn(0)
        .todayChange(0)
        .todayQna(todaysEmptyComments)
        .build();

    return APIResponse.success("function",functionDTO);
  }
}
