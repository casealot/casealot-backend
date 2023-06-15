package kr.casealot.shop.domain.ack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {
    String keyword;
    String hlKeyword;
}
