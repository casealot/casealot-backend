package kr.casealot.shop.domain.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.UUID;

public class UploadFileDTO {
    @ApiModel(value = "파일 정보 DTO", description = "파일 정보")
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @ApiModelProperty(value = "파일 ID")
        private UUID id;

        @ApiModelProperty(value = "파일 이름")
        private String name;

        @ApiModelProperty(value = "파일 타입")
        private String filetype;

        @ApiModelProperty(value = "파일 주소")
        private String url;

        @ApiModelProperty(value = "파일 크기")
        private Long fileSize;
    }
}
