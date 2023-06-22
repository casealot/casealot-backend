package kr.casealot.shop.domain.payment.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.payment.dto.PaymentDTO;
import kr.casealot.shop.domain.payment.entity.Payment;
import kr.casealot.shop.domain.payment.entity.PaymentMethod;
import kr.casealot.shop.domain.payment.entity.PaymentStatus;
import kr.casealot.shop.domain.payment.exception.NotFoundPaymentException;
import kr.casealot.shop.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

  private final CustomerRepository customerRepository;
  private final PaymentRepository paymentRepository;
  private final IamportClient iamportClient;

  @Transactional
  public PaymentDTO requestPayment(Customer customer, String orderNumber, BigDecimal amount)
      throws IamportResponseException, IOException {
    Payment payment = new Payment();
    payment.setCustomer(customer);
    payment.setOrderId(orderNumber);
    payment.setAmount(amount);
    payment.setCreateAt(LocalDateTime.now());
    try {
      PrepareData prepareReqData = new PrepareData(orderNumber, amount);
      // 사전 검증 데이터 생성 요청
      IamportResponse<Prepare> response = iamportClient.postPrepare(prepareReqData);
    } catch (IamportResponseException e) {
      // TODO: Handle exception
      throw new ConnectException(e.getMessage());
    }
    Payment savedPayment = paymentRepository.save(payment);
    return convertToDTO(savedPayment);
  }

  @Transactional
  public PaymentDTO verifyPayment(Principal principal, String orderId, String receiptId)
      throws IamportResponseException, IOException {
    Customer customer = customerRepository.findCustomerById(principal.getName());

    Payment payment = paymentRepository.findByOrderIdAndCustomer(orderId, customer)
        .orElseThrow(NotFoundPaymentException::new);

    if (!payment.getCustomer().getId().equals(customer.getId())) {
      throw new NotFoundException("Could not find payment for " + customer.getName() + ".");
    }

    log.info("payment OrderId => {}, ReceiptId => {}", payment.getOrderId(), receiptId);

    IamportResponse<AccessToken> auth = iamportClient.getAuth();
    log.info("auth Token => {}", auth.getResponse().getToken());

    try {
      IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(
          receiptId);

      if (Objects.nonNull(paymentResponse.getResponse())) {
        com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();
        log.info("===============================================================");
        log.info(paymentData.getImpUid() + " = ? " + receiptId);
        log.info(paymentData.getMerchantUid() + " = ? " + orderId);
        log.info(paymentData.getAmount() + " = ? " + payment.getAmount());
        log.info("===============================================================");
        if (receiptId.equals(paymentData.getImpUid())
            && orderId.equals(paymentData.getMerchantUid())
            && paymentData.getAmount().compareTo(paymentData.getAmount()) == 0) {
          PaymentMethod method = PaymentMethod.valueOf(paymentData.getPayMethod().toUpperCase());
          PaymentStatus status = PaymentStatus.valueOf(paymentData.getStatus().toUpperCase());
          payment.setReceiptId(paymentData.getImpUid());
          payment.setMethod(method);
          payment.setStatus(status);

          log.info("=======================성공?=======================");
          paymentRepository.save(payment);
          if (status.equals(PaymentStatus.READY)) {
            if (method.equals(PaymentMethod.VBANK)) {
              // TODO: READY + VBANK 처리 로직 구현
            } else {
              // TODO: READY + VBANK 이외의 결제 수단 처리
            }
          } else if (status.equals(PaymentStatus.PAID)) {
            payment.setPaidAt(paymentData.getPaidAt().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
            paymentRepository.save(payment);
          } else if (status.equals(PaymentStatus.FAILED)) {
            // TODO: FAILED 처리
            payment.setFailedAt(paymentData.getFailedAt().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
          } else if (status.equals(PaymentStatus.CANCELLED)) {
            // TODO: CANCELLED 처리
            payment.setCancelledAt(
                paymentData.getCancelledAt().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            payment.setCancelledAmount(paymentData.getCancelAmount());
          }
        } else {
          // TODO: 결제 정보가 일치하지 않을 때의 처리
        }
      } else {
        throw new NotFoundException("Could not find payment for " + receiptId + ".");
      }
    } catch (IamportResponseException e) {
      switch (e.getHttpStatusCode()) {
        case 404:
          throw new NotFoundException(e.getMessage() + receiptId);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }

    return convertToDTO(payment);
  }

  private PaymentDTO convertToDTO(Payment payment) {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setId(payment.getId());
    paymentDTO.setCustomerId(payment.getCustomer().getId());
    paymentDTO.setReceiptId(payment.getReceiptId());
    paymentDTO.setOrderId(payment.getOrderId());
    paymentDTO.setMethod(payment.getMethod());
    paymentDTO.setAmount(payment.getAmount());
    paymentDTO.setStatus(payment.getStatus());
    paymentDTO.setCreateAt(payment.getCreateAt());
    paymentDTO.setPaidAt(payment.getPaidAt());
    paymentDTO.setFailedAt(payment.getFailedAt());
    paymentDTO.setCancelledAmount(payment.getCancelledAmount());
    paymentDTO.setCancelledAt(payment.getCancelledAt());

    return paymentDTO;
  }
}
