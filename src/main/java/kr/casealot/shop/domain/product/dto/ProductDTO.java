package kr.casealot.shop.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.product.review.dto.ReviewProductResDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {
    /**
     * 상품 등록, 수정 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String content;
        private int price;
        private int sale;
        private String color;
        private String season;
    }


    /**
     * 상품 조회 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetRequest {
        @ApiModelProperty(value = "검색어", example = "모자")
        private String query;
        @ApiModelProperty(value = "필터 옵션")
        private List<SearchFilter> filter;
        @ApiModelProperty(value = "size", example = "5")
        private int size;
        @ApiModelProperty(value = "페이지", example = "0")
        private int page;
        @ApiModelProperty(value = "정렬 옵션", example = "0")
        private List<SortDTO> sort;
    }

    /**
     * 상품 조회 응답
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetResponse {
        private List<ProductInfo> items = new ArrayList<>();
        private Long count;
        private Long totalPages;
        private Long totalCount;
    }

    /**
     * 상품 상세 조회 응답
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private ProductInfo product;

        private List<ReviewProductResDTO> reviewList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInfo {
        private Long id;
        private String name;
        private String content;
        private UploadFile thumbnail;
        private int price;
        private int sale;
        private String color;
        private int sells;
        private double ratingCount;
        private double rating;
        private String season;
        private String type;
        private int wishCount;
        private String wishYn;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedDt;

    }

}
