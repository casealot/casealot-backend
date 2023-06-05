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
    public APIResponse<ProductDTO.CreateResponse> createProduct(
           @ApiParam(value = "상품 등록 요청 DTO") @RequestBody ProductDTO.CreateRequest createRequest)  {
        return productService.createProduct(createRequest);
    }

//TODO 수정 해야함

//    @PutMapping("/admin/product/{product_id}")
//    public ResponseEntity<String> updateProduct(@PathVariable("product_id") Long productId,
//                                                @RequestBody ProductDTO productDTO,
//                                                HttpServletRequest request) throws ChangeSetPersister.NotFoundException {
//
//        productService.updateProduct(productId, productDTO, request);
//
//        return ResponseEntity.ok("update product");
//    }

//  TODO 수정 해야함

    @DeleteMapping("/product/{product_id}")
    public APIResponse<Product> deleteProduct(
            @PathVariable("product_id") Long productId, HttpServletRequest request)
            throws ChangeSetPersister.NotFoundException {

        return productService.deleteProduct(productId, request);
    }

}
