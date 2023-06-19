package kr.casealot.shop.domain.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.order.dto.OrderDTO;
import kr.casealot.shop.domain.order.service.OrderService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"ORDER API"}, description = "주문 관련 API")
@RequestMapping("/cal/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ApiOperation(value = "주문 생성", notes = "주문을 생성한다.")
    public APIResponse<OrderDTO.Response> createOrder(
            @ApiParam(value = "주문 생성 요청 DTO - 아이템 목록") @RequestBody OrderDTO.createOrder createOrder,
                                                      Principal principal) {


        return orderService.createOrder(createOrder, principal);
    }

    @PostMapping("/{orderId}/cancel")
    @ApiOperation(value = "주문 취소", notes = "주문 취소 - 주문 상태를 취소로 변경")
    public APIResponse<Void> cancelOrder(@ApiParam(value = "주문 취소 요청 DTO - 주문 ID") @PathVariable Long orderId, Principal principal) {

        return orderService.cancelOrder(orderId, principal);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "주문 상세 조회", notes = "본인의 주문 내역 상세조회")
    public APIResponse<OrderDTO.Response> getOrderDetail(@ApiParam(value = "주문 상세 조회 DTO - 주문 ID") @PathVariable Long orderId,
                                                         Principal principal) {

        return orderService.getOrderDetail(orderId, principal);
    }

    @GetMapping
    @ApiOperation(value = "주문 목록 조회", notes = "본인의 주문 목록 조회")
    public APIResponse<List<OrderDTO.Response>> getOrderList(Principal principal) {

        return orderService.getOrderList(principal);
    }
}

