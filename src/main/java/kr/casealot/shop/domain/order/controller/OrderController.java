package kr.casealot.shop.domain.order.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"PRODUCT API"}, description = "상품 조회(검색), 상세 조회")
@RequestMapping("/cal/v1/order")
public class OrderController {
}
