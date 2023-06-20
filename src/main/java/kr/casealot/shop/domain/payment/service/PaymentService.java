package kr.casealot.shop.domain.payment.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.payment.entity.Payment;
import kr.casealot.shop.domain.payment.entity.PaymentMethod;
import kr.casealot.shop.domain.payment.entity.PaymentStatus;
import kr.casealot.shop.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static lombok.Lombok.checkNotNull;

@ConfigurationProperties(prefix = "pgmodule")
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Value("{pgmodule.app-id}")
    private String apiKey;
    @Value("{pgmodule.secret-key}")
    private String apiSecret;

    @Transactional
    public Payment requestPayment(Customer customer, String orderNumber, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setOrderId(orderNumber);
        payment.setAmount(amount);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment verifyPayment(Payment payment, Customer customer) {
        checkNotNull(payment, "payment must be provided.");
        checkNotNull(customer, "customer must be provided.");

        if (!payment.getCustomer().equals(customer)) {
            throw new NotFoundException("Could not found payment for " + customer.getName() + ".");
        }

        IamportClient iamportClient = new IamportClient(apiKey, apiSecret);
        try {
            IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(payment.getReceiptId());
            if (Objects.nonNull(paymentResponse.getResponse())) {
                com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();
                if (payment.getReceiptId().equals(paymentData.getImpUid()) && payment.getOrderId().equals(paymentData.getMerchantUid()) && payment.getAmount().compareTo(paymentData.getAmount()) == 0) {
                    PaymentMethod method = PaymentMethod.valueOf(paymentData.getPayMethod().toUpperCase());
                    PaymentStatus status = PaymentStatus.valueOf(paymentData.getStatus().toUpperCase());
                    payment.setMethod(method);
                    payment.setStatus(status);
                    paymentRepository.save(payment);
                    if (status.equals(PaymentStatus.READY)) {
                        if (method.equals(PaymentMethod.VBANK)) {
                        } else {
                        }
                    } else if (status.equals(PaymentStatus.PAID)) {
                        payment.setPaidAt(paymentData.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                        paymentRepository.save(payment);
                    } else if (status.equals(PaymentStatus.FAILED)) {

                    } else if (status.equals(PaymentStatus.CANCELLED)) {

                    }
                } else {

                }
            } else {
                throw new NotFoundException("Could not found payment for " + payment.getReceiptId() + ".");
            }
        } catch (IamportResponseException e) {
            e.printStackTrace();
            switch (e.getHttpStatusCode()) {
                case 404 -> throw new NotFoundException("Could not found payment for " + payment.getReceiptId() + ".");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payment;
    }

    @Transactional
    public Payment verifyPayment(String receiptId, String orderId, Customer customer) {
        checkNotNull(receiptId, "receiptId must be provided.");

        Optional<Payment> optionalPayment = paymentRepository.findByOrderIdAndCustomer(orderId, customer);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setReceiptId(receiptId);
            return verifyPayment(payment, customer);
        } else {
            throw new NotFoundException("Could not found payment for " + orderId + ".");
        }
    }
}
