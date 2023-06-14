package kr.casealot.shop.domain.function.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.function.dto.FunctionDTO;
import kr.casealot.shop.domain.function.dto.FunctionWeekDTO;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionService {

  private final ReviewRepository reviewRepository;

  private final CustomerRepository customerRepository;

  private final QnaRepository qnaRepository;

  public APIResponse<FunctionDTO> getTodayFunction(LocalDateTime date) {
    LocalDateTime startDate = date.toLocalDate().atStartOfDay();
    LocalDateTime endDate = date.toLocalDate().atTime(LocalTime.MAX);
    int todaysEmptyComments = qnaRepository.countByModifiedDtBetweenAndQnaCommentListIsNull(startDate, endDate);

    FunctionDTO functionDTO = new FunctionDTO().builder()
        .todayOrder(0L)
        .todayCancel(0L)
        .todayReturn(0L)
        .todayChange(0L)
        .todayQna(todaysEmptyComments)
        .build();

    return APIResponse.success("function",functionDTO);
  }

  public APIResponse<List<FunctionWeekDTO>> getWeekFunction(LocalDateTime date) {

    List<FunctionWeekDTO> functionDayList = new ArrayList<>();

    for (int i = 0; i < 7; i++) {
      LocalDateTime currentDate = date.minusDays(i);
      LocalDateTime startDate = currentDate.toLocalDate().atStartOfDay();
      LocalDateTime endDate = currentDate.toLocalDate().atTime(LocalTime.MAX);
//    int todayOrder = qnaRepository.countByModifiedDtBetween(startDate, endDate);
//    int todayCash = qnaRepository.countByModifiedDtBetween(startDate, endDate);
      int readySignIn = customerRepository.countByModifiedDtBetween(startDate, endDate);
      int todayQna = qnaRepository.countByModifiedDtBetween(startDate, endDate);
      int todayReview = reviewRepository.countByModifiedDtBetween(startDate, endDate);

      FunctionWeekDTO functionWeekDTO = new FunctionWeekDTO().builder()
          .today(currentDate)
          .todayOrder(0L)
          .todayCash(0L)
          .todaySignIn(readySignIn)
          .todayQna(todayQna)
          .todayReview(todayReview)
          .build();
      functionDayList.add(functionWeekDTO);
    }
    return APIResponse.success("function",functionDayList);
  }


}
