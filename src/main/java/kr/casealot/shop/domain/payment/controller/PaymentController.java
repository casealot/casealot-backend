package kr.casealot.shop.domain.payment.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
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
import java.util.List;

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

    @PutMapping("/{orderNumber}")
    @ApiOperation(value = "결제 검증", notes = "요청이 들어온 결제 건에 대하여 검증")
    public ResponseEntity<PaymentDTO> verifyPayment(
            Principal principal,
            @ApiParam(value = "우리가 생성한 주문 번호")@PathVariable String orderNumber,
            @ApiParam(value = "PG사 생성 주문 번호") @Valid @RequestBody String receiptId
    ) throws IamportResponseException, IOException {
        PaymentDTO payment = paymentService.verifyPayment(principal, orderNumber, receiptId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/cancel/{orderNumber}")
    @ApiOperation(value = "결제 취소", notes = "결제를 취소한다.")
    public ResponseEntity<PaymentDTO> cancelPayment(
            Principal principal,
            @ApiParam(value = "우리가 생성한 주문 번호") @PathVariable String orderNumber
            ) throws IamportResponseException, IOException {
        PaymentDTO payment = paymentService.cancelPayment(principal, orderNumber);
        return ResponseEntity.ok(payment);
    }

    @GetMapping
    @ApiOperation(value = "결제 내역", notes = "본인 결제 내역 - 유저 확인용")
    public ResponseEntity<List<PaymentDTO>> getPayment(Principal principal) {
        List<PaymentDTO> paymentDTOs = paymentService.getPayment(principal);
        return ResponseEntity.ok(paymentDTOs);
    }

//    @GetMapping("/1")
//    public APIResponse<List<PaymentDTO>> getReadyPayments() {
//
//        return paymentService.getPaymentsByStatus(PaymentStatus.READY);
//    }
//    @GetMapping("/2")
//    public ResponseEntity<List<PaymentDTO>> getPaidPayments() {
//        List<PaymentDTO> paymentDTOS = paymentService.getPaymentsByStatus(PaymentStatus.PAID);
//        return ResponseEntity.ok(paymentDTOS);
//    }
//
//    @GetMapping("/3")
//    public ResponseEntity<List<PaymentDTO>> getFailedPayments() {
//        List<PaymentDTO> paymentDTOS = paymentService.getPaymentsByStatus(PaymentStatus.FAILED);
//        return ResponseEntity.ok(paymentDTOS);
//    }
//
//    @GetMapping("/4")
//    public ResponseEntity<List<PaymentDTO>> getCanceledPayments() {
//        List<PaymentDTO> paymentDTOS = paymentService.getPaymentsByStatus(PaymentStatus.CANCELED);
//        return ResponseEntity.ok(paymentDTOS);
//    }
}
