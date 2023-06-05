package kr.casealot.shop.domain.product.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import lombok.*;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {
    /**
     * 상품 등록 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest{
        private String name;
        private String content;
        private int price;
        private int sale;
        private String color;
        private String season;
        private String type;
    }

    /**
     * 상품 등록 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest{
        private Long id;
        private String name;
        private String content;
        private int price;
        private int sale;
        private String color;
        private String season;
        private String type;
    }

    /**
     * 상품 조회 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetRequest{
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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetResponse {
        private List<Product> items = new ArrayList<>();
        private Long count;
        private Long totalPages;
        private Long totalCount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Product product;

        private List<ReviewResDTO> reviewList;
    }

}
