package kr.casealot.shop.domain.payment.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.payment.dto.CancelPaymentReq;
import kr.casealot.shop.domain.payment.dto.PaymentDTO;
import kr.casealot.shop.domain.payment.entity.PaymentRequest;
import kr.casealot.shop.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;

@Service
@Api(tags = {"PAYMENT API"}, description = "결제 관련 API")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cal/v1/verifyIamport")
public class PaymentController {
    private final CustomerRepository customerRepository;
    private final PaymentService paymentService;

    @PostMapping
    @ApiOperation(value = "결제 요청, 사전 검증")
    public ResponseEntity<PaymentDTO> requestPayment(
            Principal principal,
            @ApiParam(value = "가격, 주문번호") @Valid @RequestBody PaymentRequest request
    ) throws IamportResponseException, IOException {
        Customer customer = customerRepository.findCustomerById(principal.getName());
        BigDecimal amount = new BigDecimal(request.getAmount());
        PaymentDTO paymentDTO = paymentService.requestPayment(customer, request.getOrderNumber(), amount);
        return ResponseEntity.ok(paymentDTO);
    }

    @PutMapping("/{orderId}")
    @ApiOperation(value = "결제 검증", notes = "요청이 들어온 결제 건에 대하여 검증")
    public ResponseEntity<PaymentDTO> verifyPayment(
            Principal principal,
            @ApiParam(value = "우리가 생성한 주문 번호")@PathVariable String orderId,
            @ApiParam(value = "PG사 생성 주문 번호") @Valid @RequestBody String receiptId
    ) throws IamportResponseException, IOException {
        PaymentDTO payment = paymentService.verifyPayment(principal, orderId, receiptId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "결제 취소", notes = "결제를 취소한다.")
    public ResponseEntity<PaymentDTO> cancelPayment(
            Principal principal,
            @ApiParam(value = "PG사 생성 주문 번호, 우리가 생성한 주문 번호") @RequestBody CancelPaymentReq cancelPaymentReq
            ) throws IamportResponseException, IOException {
        PaymentDTO payment = paymentService.cancelPayment(principal, cancelPaymentReq);
        return ResponseEntity.ok(payment);
    }
}
