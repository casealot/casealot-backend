package kr.casealot.shop.domain.function.controller;

import io.swagger.annotations.Api;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import kr.casealot.shop.domain.function.dto.FunctionDTO;
import kr.casealot.shop.domain.function.dto.FunctionWeekDTO;
import kr.casealot.shop.domain.function.service.FunctionService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public APIResponse<FunctionDTO> getDailyOrderData() {
    return functionService.getTodayFunction(today);
  }

  @GetMapping("/weekly")
  public APIResponse<List<FunctionWeekDTO>> getWeeklyOrderData() {
    return functionService.getWeekFunction(today);
  }
}
