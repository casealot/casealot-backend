package kr.casealot.shop.domain.qna.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDTO {
    private String title;
    private String content;
    private String photoUrl;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
}
