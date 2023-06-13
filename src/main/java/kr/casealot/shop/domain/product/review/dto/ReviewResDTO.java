package kr.casealot.shop.domain.product.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResDTO {
    private Long id;
    private String customerName; //고객 이름
    private Double rating; //별점
    private String reviewText; //리뷰 내용
    private String available; //수정, 삭제가능여부
    @JsonProperty
    private List<ReviewCommentResDTO> reviewCommentList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDt;
}
