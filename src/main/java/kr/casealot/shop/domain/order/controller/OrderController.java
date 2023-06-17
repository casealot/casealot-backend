package kr.casealot.shop.domain.order.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.order.dto.OrderDTO;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.order.service.OrderService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public APIResponse<OrderDTO.Response> createOrder(@RequestBody OrderDTO.Request request,
                                                      Principal principal){


        return orderService.createOrder(request, principal);
    }

    @PostMapping("/{orderId}/cancel")
    public APIResponse<Void> cancelOrder(@PathVariable Long orderId, Principal principal){

        return orderService.cancelOrder(orderId, principal);
    }

    @GetMapping("/{orderId}")
    public APIResponse<OrderDTO.Response> getOrderDetail(@PathVariable Long orderId,
                                                         Principal principal) {

        return orderService.getOrderDetail(orderId, principal);
    }

    @GetMapping
    public APIResponse<List<OrderDTO.Response>> getOrderList(Principal principal) {

        return orderService.getOrderList(principal);
    }
}

