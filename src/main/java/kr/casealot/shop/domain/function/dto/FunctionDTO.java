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

  private int newOrder;
  private int cancelManage;
  private int cancelRequest;
  private int returnManage;
  private int changManage;
  private int readyAnswer;
}
