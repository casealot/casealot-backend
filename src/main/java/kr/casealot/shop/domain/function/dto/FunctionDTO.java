package kr.casealot.shop.domain.function.dto;

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
public class FunctionDTO {

  private long todayOrder;
  private long todayCancel;
  private long todayReturn;
  private long todayChange;
  private long todayQna;
}
