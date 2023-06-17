package kr.casealot.shop.domain.order.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"ORDER API"}, description = "상품 주문")
@RequestMapping("/cal/v1/order")
public class OrderController {
    /**
     * 주문 정보 조회
     */

    /**
     * 주문
     */
    @PostMapping
    public APIResponse order() {
        return APIResponse.success("");
    }

    /**
     * 주문 취소
     */
    @DeleteMapping
    public APIResponse cancel() {
        return APIResponse.success("");
    }
}
