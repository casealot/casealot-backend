package kr.casealot.shop.domain.product.controller;

import kr.casealot.shop.domain.product.dto.ProductListDTO;
import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1")
public class ProductController {

    private final ProductService productService;
    private final String API_NAME = "product";

    /**
     * 전체 상품 조회
     * @return
     */
    @GetMapping("/product")
    public ResponseEntity<ProductListDTO> getProductList(
            @RequestBody ProductReqDTO productReqDTO
            ) {
        Pageable pageable = PageRequest.of(productReqDTO.getPage()
                , productReqDTO.getSize());
        ProductListDTO productList = productService.findAll(pageable);
        return ResponseEntity.ok(productList);
    }


    /**
     * 상품 조회
     * @param id
     * @return
     */
//    @GetMapping("/product/{id}")
//    public APIResponse getProductDetail(
//            @PathVariable Long id) {
//        Product product = productRepository.findById(id).orElseGet(Product::new);
//        return APIResponse.success(API_NAME, product);
//    }
//
//    /**
//     * 상품 등록
//     * @param productReqDTO
//     * @return
//     */
//    @PostMapping("/product")
//    public APIResponse addProduct(
//            @RequestBody Product reqProduct) {
//        Product product = productRepository.save(reqProduct);
//        return APIResponse.success(API_NAME, product);
//    }
//
//    /**
//     * 상품 수정
//     * @param id
//     * @param productReqDTO
//     * @return
//     */
//    @PutMapping("/product/{id}")
//    public APIResponse updateProduct(
//            @PathVariable String id
//            , @RequestBody ProductReqDTO productReqDTO) {
//
//        return APIResponse.success(API_NAME, new Product());
//    }
//
//    @DeleteMapping("/product/{id}")
//    public APIResponse deleteProduct(
//            @PathVariable String id) {
//        productRepository.deleteById(id);
//        return APIResponse.success(API_NAME, new Product());
//    }
}
