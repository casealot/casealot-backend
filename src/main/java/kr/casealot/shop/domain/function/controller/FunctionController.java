package kr.casealot.shop.domain.function.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"FUNCTION API"}, description = "기능 통계 관련 API")
@RequestMapping("/cal/v1/function")
public class FunctionController {

}
