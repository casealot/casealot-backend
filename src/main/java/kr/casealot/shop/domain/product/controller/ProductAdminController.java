package kr.casealot.shop.domain.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.DuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"PRODUCT ADMIN API"}, description = "상품 등록 ")
@RequestMapping("/cal/v1/admin/product")
public class ProductAdminController {
    private final ProductService productService;

    @ApiOperation(value = "ADMIN 상품 등록", notes = "상품을 등록한다.")
    @PostMapping
    public APIResponse<Product> createProduct(
           @ApiParam(value = "상품 등록 요청 DTO") @RequestBody ProductDTO.Request createRequest) throws DuplicateException {
        return productService.createProduct(createRequest);
    }

    @ApiOperation(value = "ADMIN 상품 정보 수정", notes = "상품 정보를 수정 한다.")
    @PutMapping("/{id}")
    public APIResponse<Product> updateProduct(
            @PathVariable Long id,
            @ApiParam(value = "상품 수정 요청 DTO") @RequestBody ProductDTO.Request updateRequest) throws Exception {
        return productService.updateProduct(id, updateRequest);
    }

    @ApiOperation(value = "ADMIN 상품 삭제", notes = "상품을 삭제한다.")
    @DeleteMapping("/{id}")
    public APIResponse deleteProduct(
            @ApiParam(value = "상품 ID") @PathVariable Long id) throws Exception {
        return productService.deleteProduct(id);
    }

}
