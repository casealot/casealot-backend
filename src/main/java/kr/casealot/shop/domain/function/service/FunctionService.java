package kr.casealot.shop.domain.function.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.function.dto.FunctionDTO;
import kr.casealot.shop.domain.function.dto.FunctionQnaDTO;
import kr.casealot.shop.domain.function.dto.FunctionReviewDTO;
import kr.casealot.shop.domain.function.dto.FunctionSalesDTO;
import kr.casealot.shop.domain.function.dto.FunctionWeekDTO;
import kr.casealot.shop.domain.function.dto.MyPageDTO;
import kr.casealot.shop.domain.order.delivery.dto.DeliveryStatus;
import kr.casealot.shop.domain.order.dto.OrderStatus;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionService {

  private final ProductRepository productRepository;

  private final ReviewRepository reviewRepository;

  private final CustomerRepository customerRepository;

  private final QnaRepository qnaRepository;

  private final OrderRepository orderRepository;

  public APIResponse<FunctionDTO> getTodayFunction(LocalDateTime date) {
    LocalDateTime startDate = date.toLocalDate().atStartOfDay();
    LocalDateTime endDate = date.toLocalDate().atTime(LocalTime.MAX);
    int todaysEmptyComments = qnaRepository.countByModifiedDtBetweenAndQnaCommentListIsNull(
        startDate, endDate);
    int todayOrder = orderRepository.countByOrderDtBetweenAndOrderStatus(startDate, endDate,
        OrderStatus.COMPLETE);
    int todayCancel = orderRepository.getCanceledOrderCountBetween(startDate, endDate);
    int todayChange = orderRepository.getChangedOrderCountBetween(startDate, endDate);

    FunctionDTO functionDTO = new FunctionDTO().builder()
        .todayOrder(todayOrder)
        .todayCancel(todayCancel)
//        .todayReturn(0L)
        .todayChange(todayChange)
        .todayQna(todaysEmptyComments)
        .build();

    return APIResponse.success("function", functionDTO);
  }

  public APIResponse<List<FunctionWeekDTO>> getWeekFunction(LocalDateTime date) {

    List<FunctionWeekDTO> functionDayList = new ArrayList<>();

    for (int i = 0; i < 7; i++) {
      LocalDateTime currentDate = date.minusDays(i);
      LocalDateTime startDate = currentDate.toLocalDate().atStartOfDay();
      LocalDateTime endDate = currentDate.toLocalDate().atTime(LocalTime.MAX);
      int todayOrder = orderRepository.countByOrderDtBetweenAndOrderStatus(startDate, endDate,
          OrderStatus.COMPLETE);
      long todayCash = orderRepository.getTotalAmountBetween(startDate, endDate);
      int readySignIn = customerRepository.countByModifiedDtBetween(startDate, endDate);
      int todayQna = qnaRepository.countByModifiedDtBetween(startDate, endDate);
      int todayReview = reviewRepository.countByModifiedDtBetween(startDate, endDate);

      FunctionWeekDTO functionWeekDTO = new FunctionWeekDTO().builder()
          .today(currentDate)
          .todayOrder(todayOrder)
          .todayCash(todayCash)
          .todaySignIn(readySignIn)
          .todayQna(todayQna)
          .todayReview(todayReview)
          .build();
      functionDayList.add(functionWeekDTO);
    }
    return APIResponse.success("function", functionDayList);
  }

  public APIResponse<List<FunctionQnaDTO>> getQnaFunction() {

    List<FunctionQnaDTO> functionQnaDTOList = new ArrayList<>();
    List<Qna> qnaList = qnaRepository.findAllByOrderByModifiedDtDesc();

    // 최대 30개의 QNA만 가져오도록 제한
    int qnaLimit = 30;
    int qnaCount = 0;

    for (Qna qna : qnaList) {
      if (qnaCount >= qnaLimit) {
        break;
      }

      if (qna.getCustomer().getProfileImg() != null) {
        FunctionQnaDTO functionQnaDTO = FunctionQnaDTO.builder()
            .id(qna.getId())
            .profileImg(qna.getCustomer().getProfileImg().getUrl())
            .title(qna.getTitle())
            .customerId(qna.getCustomer().getId())
            .modifiedDt(qna.getModifiedDt())
            .build();
        functionQnaDTOList.add(functionQnaDTO);
      } else {
        FunctionQnaDTO functionQnaDTO = FunctionQnaDTO.builder()
            .id(qna.getId())
            .profileImg(null)
            .title(qna.getTitle())
            .customerId(qna.getCustomer().getId())
            .modifiedDt(qna.getModifiedDt())
            .build();
        functionQnaDTOList.add(functionQnaDTO);
      }
      qnaCount++;
    }

    return APIResponse.success("function", functionQnaDTOList);
  }


  public APIResponse<List<FunctionReviewDTO>> getReviewFunction() {

    List<FunctionReviewDTO> functionReviewDTOList = new ArrayList<>();
    List<Review> reviewList = reviewRepository.findAllByOrderByModifiedDtDesc();

    // 최대 30개의 Review만 가져오도록 제한
    int reviewLimit = 30;
    int reviewCount = 0;

    for (Review review : reviewList) {
      if (reviewCount >= reviewLimit) {
        break;
      }

      Product product = review.getProduct();
      if (product != null) {
        UploadFile thumbnail = product.getThumbnail();
        FunctionReviewDTO functionReviewDTO;
        if (thumbnail != null) {
          functionReviewDTO = FunctionReviewDTO.builder()
              .id(review.getSeq())
              .productName(product.getName())
              .productThumbnail(thumbnail.getUrl())
              .reviewText(review.getReviewText())
              .customerId(review.getCustomer().getId())
              .modifiedDt(review.getModifiedDt())
              .build();

        } else {
          functionReviewDTO = FunctionReviewDTO.builder()
              .id(review.getSeq())
              .productName(product.getName())
              .productThumbnail(null)
              .reviewText(review.getReviewText())
              .customerId(review.getCustomer().getId())
              .modifiedDt(review.getModifiedDt())
              .build();

        }
        functionReviewDTOList.add(functionReviewDTO);
        reviewCount++;
      }
    }

    return APIResponse.success("function", functionReviewDTOList);
  }


  public APIResponse<List<FunctionSalesDTO>> getSalesAndOrderFunction(LocalDateTime date) {
    List<FunctionSalesDTO> functionSalesList = new ArrayList<>();

    for (int i = 0; i < 7; i++) {
      LocalDateTime currentDate = date.minusDays(i);
      LocalDateTime startDate = currentDate.toLocalDate().atStartOfDay();
      LocalDateTime endDate = currentDate.toLocalDate().atTime(LocalTime.MAX);
      long todayCash = orderRepository.getTotalAmountBetween(startDate, endDate);
      int todayOrder = orderRepository.countByOrderDtBetweenAndOrderStatus(startDate, endDate,
          OrderStatus.COMPLETE);


      FunctionSalesDTO functionSalesDTO = new FunctionSalesDTO().builder()
          .today(currentDate)
          .orderCounts(todayOrder)
          .todaySales(todayCash)
          .build();
      functionSalesList.add(functionSalesDTO);
    }
    return APIResponse.success("function", functionSalesList);
  }

  public APIResponse<MyPageDTO> getMyPageFunction(Principal principal) {
    Customer customer =customerRepository.findById(principal.getName());

    MyPageDTO myPageDTO = MyPageDTO.builder()
        .profileImg(customer.getProfileImg())
        .ready(orderRepository.countByCustomerSeqAndDeliveryStatus(customer.getSeq(),
            DeliveryStatus.READY))
        .start(orderRepository.countByCustomerSeqAndDeliveryStatus(customer.getSeq(),
            DeliveryStatus.DELIVERING))
        .finish(orderRepository.countByCustomerSeqAndDeliveryStatus(customer.getSeq(),
            DeliveryStatus.COMPLETE))
        .canceled(orderRepository.countByCustomerSeqAndDeliveryStatus(customer.getSeq(),
            DeliveryStatus.CANCELED))
        .build();
    return APIResponse.success("function", myPageDTO);
  }
}
