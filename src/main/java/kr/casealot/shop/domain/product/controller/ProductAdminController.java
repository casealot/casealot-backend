package kr.casealot.shop.domain.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"PRODUCT ADMIN API"}, description = "상품 등록 ")
@RequestMapping("/cal/v1/admin")
public class ProductAdminController {
    private final ProductService productService;

    @ApiOperation(value = "ADMIN 상품 등록", notes = "상품을 등록한다.")
    @PostMapping("/product")
    public APIResponse<Product> createProduct(
           @ApiParam(value = "상품 등록 요청 DTO") @RequestBody ProductDTO.CreateRequest createRequest)  {
        return productService.createProduct(createRequest);
    }


    @PutMapping("/admin/product/{product_id}")
    public APIResponse<Product>  updateProduct(
            @PathVariable("product_id") Long productId,
            @ApiParam(value = "상품 수정 요청 DTO") @RequestBody ProductDTO.UpdateRequest updateRequest){
        return productService.updateProduct(updateRequest);
    }

    @ApiOperation(value = "ADMIN 상품 삭제", notes = "상품을 삭제한다.")
    @DeleteMapping("/product/{product_id}")
    public APIResponse deleteProduct(
            @ApiParam(value = "상품 ID") @PathVariable("product_id") Long productId) throws Exception {
        return productService.deleteProduct(productId);
    }

}
