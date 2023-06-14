package kr.casealot.shop.domain.ack.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ACKResponse {
    List<Keyword> item;
    int totalCount;
}
