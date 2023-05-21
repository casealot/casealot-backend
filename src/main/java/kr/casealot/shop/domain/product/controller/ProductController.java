package kr.casealot.shop.domain.product.controller;

import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1")
public class ProductController {

    private final ProductRepository productRepository;
    private final String API_NAME = "product";

    /**
     * 전체 상품 조회
     * @return
     */
    @GetMapping("/product")
    public APIResponse getProductList() {
        List<Product> productList = productRepository.findAll();
        return APIResponse.success(API_NAME, productList);
    }

    /**
     * 상품 조회
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    public APIResponse getProductDetail(
            @PathVariable String id) {
        Product product = productRepository.findById(id);
        return APIResponse.success(API_NAME, product);
    }

    /**
     * 상품 등록
     * @param productReqDTO
     * @return
     */
    @PostMapping("/product")
    public APIResponse addProduct(
            @RequestBody ProductReqDTO productReqDTO) {
        return APIResponse.success(API_NAME, new Product());
    }

    /**
     * 상품 수정
     * @param id
     * @param productReqDTO
     * @return
     */
    @PutMapping("/product/{id}")
    public APIResponse updateProduct(
            @PathVariable String id
            , @RequestBody ProductReqDTO productReqDTO) {
        return APIResponse.success(API_NAME, new Product());
    }

    @DeleteMapping("/product/{id}")
    public APIResponse deleteProduct(
            @PathVariable String id) {
        productRepository.deleteById(id);
        return APIResponse.success(API_NAME, new Product());
    }
}
