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

  private int todayOrder;
  private int todayCancel;
  private int todayReturn;
  private int todayChange;
  private int todayQna;
}
