package kr.casealot.shop.domain.function.dto;

import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.order.delivery.dto.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {
  //배송준비 배송중 배송완료
  private Long ready;
  private Long start;
  private Long finish;
  private Long canceled;
  private UploadFile profileImg;
}
