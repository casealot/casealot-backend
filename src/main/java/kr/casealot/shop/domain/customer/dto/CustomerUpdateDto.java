package kr.casealot.shop.domain.customer.dto;

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
public class CustomerUpdateDto {
  private String email;
  private String name;
  private String phoneNumber;
  private String address;
  private String addressDetail;
  private String profileImageUrl;
}
