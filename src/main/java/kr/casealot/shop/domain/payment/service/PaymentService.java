package kr.casealot.shop.domain.payment.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.payment.entity.Payment;
import kr.casealot.shop.domain.payment.entity.PaymentMethod;
import kr.casealot.shop.domain.payment.entity.PaymentStatus;
import kr.casealot.shop.domain.payment.exception.NotFoundPaymentException;
import kr.casealot.shop.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.HttpException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static lombok.Lombok.checkNotNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    @Transactional
    public Payment requestPayment(Customer customer, String orderNumber, BigDecimal amount) throws IamportResponseException, IOException {
        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setOrderId(orderNumber);
        payment.setAmount(amount);
        try {
            PrepareData prepareReqData = new PrepareData(orderNumber, amount);
            //사전 검증 데이터 생성 요청
            IamportResponse<Prepare> response = iamportClient.postPrepare(prepareReqData);
        } catch (IamportResponseException e) {
            // TODO 예외처리 메시지 정의 필요
            throw new ConnectException(e.getMessage());
        }
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment verifyPayment(Payment payment, Customer customer) {

        if (!payment.getCustomer().getId().equals(customer.getId())) {
            throw new NotFoundException("Could not found payment for " + customer.getName() + ".");
        }
        log.info("payment OrderId => {}, ReceiptId => {}"
                , payment.getOrderId(), payment.getReceiptId());
        try {
            IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(payment.getReceiptId());

            if (Objects.nonNull(paymentResponse.getResponse())) {
                com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();
                if (payment.getReceiptId().equals(paymentData.getImpUid())
                        && payment.getOrderId().equals(paymentData.getMerchantUid())
                        && payment.getAmount().compareTo(paymentData.getAmount()) == 0) {
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
            switch (e.getHttpStatusCode()) {
                case 404 -> throw new NotFoundException(e.getMessage() + payment.getReceiptId());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return payment;
    }

    @Transactional(readOnly = true)
    public Payment verifyPayment(String receiptId, String orderId, Customer customer) {

        Payment payment = paymentRepository.findByOrderIdAndCustomer(orderId, customer)
                .orElseThrow(NotFoundPaymentException::new);

        Customer buyer = payment.getCustomer();
        payment.setReceiptId(receiptId);

        return verifyPayment(payment, customer);
    }
}
