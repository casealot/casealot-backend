package kr.casealot.shop.domain.function.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import kr.casealot.shop.domain.function.dto.FunctionDTO;
import kr.casealot.shop.domain.function.dto.FunctionQnaDTO;
import kr.casealot.shop.domain.function.dto.FunctionReviewDTO;
import kr.casealot.shop.domain.function.dto.FunctionSalesDTO;
import kr.casealot.shop.domain.function.dto.FunctionWeekDTO;
import kr.casealot.shop.domain.function.service.FunctionService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"FUNCTION API"}, description = "기능 통계 관련 API")
@RequestMapping("/cal/v1/function")
public class FunctionController {

  private final FunctionService functionService;

  //오늘 기준 시간 00시 00분 00초
  LocalDateTime today = LocalDateTime.now().with(LocalTime.MIN);

  @GetMapping("/daily")
  @ApiOperation(value = "상단 오늘의 할일", notes = "오늘의 할일에 들어갈 데이터를 제공한다.")
  public APIResponse<FunctionDTO> getDailyOrderData() {
    return functionService.getTodayFunction(today);
  }

  @GetMapping("/weekly")
  @ApiOperation(value = "7일치 일자별 요약", notes = "일자별 요약에 들어갈 데이터를 제공한다.")
  public APIResponse<List<FunctionWeekDTO>> getWeeklyOrderData() {
    return functionService.getWeekFunction(today);
  }

  @GetMapping("/qna")
  @ApiOperation(value = "전체 QNA 최신순 목록", notes = "QNA 요약에 들어갈 데이터를 제공한다.")
  public APIResponse<List<FunctionQnaDTO>> getQnaData() {
    return functionService.getQnaFunction();
  }

  @GetMapping("/review")
  @ApiOperation(value = "전체 리뷰 최신순 목록", notes = "리뷰 요약에 들어갈 데이터를 제공한다.")
  public APIResponse<List<FunctionReviewDTO>> getReviewData() {
    return functionService.getReviewFunction();
  }

  @GetMapping("/sales")
  @ApiOperation(value = "주간 판매 요약", notes = "주간 판매액을 일자별로 나열하여 그래프로 표기할 수 있게 한다.")
  public APIResponse<List<FunctionSalesDTO>> getSalesData() {
    return functionService.getSalesFunction(today);
  }
}
