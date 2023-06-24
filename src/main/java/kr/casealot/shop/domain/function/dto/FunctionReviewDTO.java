package kr.casealot.shop.domain.function.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kr.casealot.shop.domain.file.entity.UploadFile;
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
public class FunctionReviewDTO {

  //review 번호 / 제목 / 이름 / 날짜
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String productThumbnail;

  private String reviewText;

  private String customerId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime modifiedDt;

}
