package kr.casealot.shop.domain.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.order.dto.OrderDTO;
import kr.casealot.shop.domain.order.dto.OrderStatus;
import kr.casealot.shop.domain.order.exception.OrderAlreadyCompleteException;
import kr.casealot.shop.domain.order.exception.OrderStatusException;
import kr.casealot.shop.domain.order.service.OrderService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ApiOperation(value = "주문 취소", notes = "주문을 취소한다.")
    public APIResponse<OrderDTO.Response> cancelOrder(@ApiParam(value = "주문 취소 요청 DTO - 주문 ID") @PathVariable Long orderId, Principal principal) {

        return orderService.cancelOrder(orderId, principal);
    }

    @PostMapping("/{orderId}/cartComplete")
    @ApiOperation(value = "주문 완료", notes = "주문 완료(장바구니) - 주문 상태를 완료로 변경, 장바구니에서 상품 삭제")
    public APIResponse<OrderDTO.Response> completeCartOrder(@ApiParam(value = "주문 완료 요청 DTO - 주문 ID") @PathVariable Long orderId, Principal principal) {

        return orderService.completeCartOrder(orderId, principal);
    }

    @PostMapping("/{orderId}/directComplete")
    @ApiOperation(value = "주문 완료", notes = "주문 완료(바로결제) - 주문 상태를 완료로 변경")
    public APIResponse<OrderDTO.Response> completeDirectOrder(@ApiParam(value = "주문 완료 요청 DTO - 주문 ID") @PathVariable Long orderId, Principal principal) {

        return orderService.completeDirectOrder(orderId, principal);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "주문 상세 조회", notes = "본인의 주문 내역 상세조회")
    public APIResponse<OrderDTO.Response> getOrderDetail(@ApiParam(value = "주문 상세 조회 DTO - 주문 ID") @PathVariable Long orderId,
                                                         Principal principal) {

        return orderService.getOrderDetail(orderId, principal);
    }


    //합쳐서 인자로 상태코드 넘겨서 메서드 하나로 해결해도 될듯.

    @GetMapping("/list")
    @ApiOperation(value = "주문 목록 조회", notes = "본인의 주문 목록 조회")
    public APIResponse<List<OrderDTO.Response>> getOrderList(Principal principal) {

        return orderService.getOrderList(principal);
    }


    @GetMapping("/list/{status}")
    @ApiOperation(value = "주문 목록 조회", notes = "본인의 주문 상태별 목록 조회")
    public APIResponse<List<OrderDTO.Response>> getOrderListByStatus(
            @ApiParam(value = "주문 상태 ex) complete,change,cancel") @PathVariable("status") String status,
            Principal principal) {

        OrderStatus orderStatus = switch (status) {
            case "complete" -> OrderStatus.COMPLETE;
            case "change" -> OrderStatus.CHANGE;
            case "cancel" -> OrderStatus.CANCEL;
            default -> throw new OrderStatusException();
        };

        return orderService.getOrderListByStatus(principal, orderStatus);
    }
}

