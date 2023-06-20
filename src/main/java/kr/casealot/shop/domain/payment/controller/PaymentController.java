package kr.casealot.shop.domain.payment.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import io.swagger.annotations.Api;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.domain.payment.entity.Payment;
import kr.casealot.shop.domain.payment.entity.PaymentRequest;
import kr.casealot.shop.domain.payment.service.PaymentService;
import kr.casealot.shop.global.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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
    public ResponseEntity<?> requestPayment(
            Principal principal,
            @Valid @RequestBody PaymentRequest request
    ) throws IamportResponseException, IOException {
        Customer customer = customerRepository.findCustomerById(principal.getName());
        BigDecimal amount = new BigDecimal(request.getAmount());
        Payment payment = paymentService.requestPayment(customer, request.getOrderNumber(), amount);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("{orderId}")
    public ResponseEntity<?> verifyPayment(
            Principal principal,
            @PathVariable String orderId,
            @Valid @RequestBody String receiptId
    ) {
        Customer customer = customerRepository.findById(principal.getName());
        Payment payment = paymentService.verifyPayment(receiptId, orderId, customer);
        return ResponseEntity.ok(payment);
    }
}
