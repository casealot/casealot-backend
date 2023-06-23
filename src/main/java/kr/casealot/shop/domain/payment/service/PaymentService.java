package kr.casealot.shop.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.payment.dto.PaymentDTO;
import kr.casealot.shop.domain.payment.entity.Payment;
import kr.casealot.shop.domain.payment.entity.PaymentMethod;
import kr.casealot.shop.domain.payment.entity.PaymentStatus;
import kr.casealot.shop.domain.payment.exception.*;
import kr.casealot.shop.domain.payment.repository.PaymentRepository;
import kr.casealot.shop.global.exception.PermissionException;
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
  private final OrderRepository orderRepository;

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
      log.error(e.getMessage());
      throw new ConnectException();
    }
    Payment savedPayment = paymentRepository.save(payment);
    return convertToDTO(savedPayment);
  }

  @Transactional
  public PaymentDTO verifyPayment(Principal principal, String orderId, String receiptId)
      throws IamportResponseException, IOException {
    Customer customer = customerRepository.findCustomerById(principal.getName());
    Order order = orderRepository.findByOrderNumber(orderId);
    Payment payment = paymentRepository.findByOrderIdAndCustomer(orderId, customer)
        .orElseThrow(NotFoundPaymentException::new);

    if (!payment.getCustomer().getId().equals(customer.getId())) {
      throw new PermissionException();
    }

    log.info("payment OrderId => {}, ReceiptId => {}", payment.getOrderId(), receiptId);

    IamportResponse<AccessToken> auth = iamportClient.getAuth();
    log.info("auth Token => {}", auth.getResponse().getToken());

    try {
      IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(receiptId);

      if (Objects.nonNull(paymentResponse.getResponse())) {
        com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();
        log.info("===============================================================");
        log.info(paymentData.getImpUid() + " = ? " + receiptId + " : " + (paymentData.getImpUid().equals(receiptId)));
        log.info(paymentData.getMerchantUid() + " = ? " + orderId + " : " + (paymentData.getMerchantUid().equals(orderId)));
        log.info(paymentData.getAmount() + " = ? " + payment.getAmount() + " : " +  (Objects.equals(paymentData.getAmount(), payment.getAmount())));
        log.info("===============================================================");
        if (receiptId.equals(paymentData.getImpUid())
            && orderId.equals(paymentData.getMerchantUid())
            && payment.getAmount().compareTo(paymentData.getAmount()) == 0) {
          PaymentMethod method = PaymentMethod.valueOf(paymentData.getPayMethod().toUpperCase());
          PaymentStatus status = PaymentStatus.valueOf(paymentData.getStatus().toUpperCase());
          payment.setReceiptId(paymentData.getImpUid());
          payment.setMethod(method);
          payment.setStatus(status);
          payment.setOId(order.getId());
          order.setPayment(payment);

          orderRepository.save(order);
          paymentRepository.save(payment);
          if (status.equals(PaymentStatus.READY)) {
            if (method.equals(PaymentMethod.VBANK)) {
                throw new PaymentRequiredException();
            } else {
                throw new PaymentRequiredException();
            }
          } else if (status.equals(PaymentStatus.PAID)) {
            payment.setPaidAt(paymentData.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            paymentRepository.save(payment);
          } else if (status.equals(PaymentStatus.FAILED)) {
            throw new PaymentFailedException();
          } else if (status.equals(PaymentStatus.CANCELLED)) {
            throw new PaymentCanceledException();
          }
        } else {
          // 결제 정보가 일치하지 않을 경우
          throw new NotFoundPaymentException();
        }
      } else {
        // receiptId 로 결제 정보 찾을 수 없는 경우
        throw new NotFoundPaymentException();
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }

    return convertToDTO(payment);
  }

  private PaymentDTO convertToDTO(Payment payment) {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setId(payment.getId());
    paymentDTO.setOId(payment.getOId());
    paymentDTO.setCustomerId(payment.getCustomer().getId());
    paymentDTO.setReceiptId(payment.getReceiptId());
    paymentDTO.setOrderId(payment.getOrderId());
    paymentDTO.setMethod(payment.getMethod());
    paymentDTO.setAmount(payment.getAmount());
    paymentDTO.setStatus(payment.getStatus());
    paymentDTO.setCreateAt(payment.getCreateAt());
    paymentDTO.setPaidAt(payment.getPaidAt());
    paymentDTO.setCancelledAmount(payment.getCancelledAmount());
    paymentDTO.setCancelledAt(payment.getCancelledAt());

    return paymentDTO;
  }
}
