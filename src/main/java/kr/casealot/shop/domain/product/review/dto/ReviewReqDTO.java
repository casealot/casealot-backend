package kr.casealot.shop.domain.product.review.dto;

import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewReqDTO{
    //토큰 통해 작성 + 상품 페이지에 들어가서 작성 (사람과, 상품에 대한 정보 없어도 됨)
//    private Long productSeq; //어떤 상품인지에 대한 정보
    private Double rating; //별점
    private String reviewText; //리뷰 내용
}
