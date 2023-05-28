package kr.casealot.shop.domain.product.controller;

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
@RequestMapping("/cal/v1")
public class ProductController {

    private final ProductService productService;
    /**
     * 전체 상품 조회
     * @return
     */
    @GetMapping("/product")
    public ResponseEntity<ProductResDTO> getProductList(
            @RequestBody ProductReqDTO productReqDTO
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
            @PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

}
