package kr.casealot.shop.domain.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.product.dto.ProductResDTO;
import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"PRODUCT API"}, description = "상품 조회(검색), 상세 조회")
@RequestMapping("/cal/v1")
public class ProductController {

    private final ProductService productService;
    /**
     * 전체 상품 조회
     * @return
     */
    @GetMapping("/product")
    @ApiOperation(value = "상품 검색 및 조회", notes = "상품 정보를 갖고온다.")
    public ResponseEntity<ProductResDTO> getProductList(
            @ApiParam(value = "상품 요청 DTO") @RequestBody ProductReqDTO productReqDTO
            ) {
        ProductResDTO productList = productService.findAllSearch(productReqDTO);
        return ResponseEntity.ok(productList);
    }

    /**
     * 상품 조회
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductDetail(
            @ApiParam(value = "상품 요청 DTO")@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

}
