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
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"PRODUCT API"}, description = "상품 조회(검색), 상세 조회")
@RequestMapping("/cal/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/product")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO,
                                                HttpServletRequest request){
        productService.createProduct(productDTO, request);
        return ResponseEntity.ok("create product");
    }

    @PutMapping("/admin/product/{product_id}")
    public ResponseEntity<String> updateProduct(@PathVariable("product_id") Long productId,
                                                @RequestBody ProductDTO productDTO,
                                                HttpServletRequest request) throws NotFoundException {

        productService.updateProduct(productId, productDTO, request);

        return ResponseEntity.ok("update product");
    }

    @DeleteMapping("/admin/product/{product_id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("product_id") Long productId, HttpServletRequest request)
            throws NotFoundException {

        productService.deleteProduct(productId, request);
        return ResponseEntity.ok("delete product");
    }

    /**
     * 전체 상품 조회
     * @return
     */
    @GetMapping("/product")
    @ApiOperation(value = "상품 검색 및 조회", notes = "상품 정보를 갖고온다.")
    public APIResponse getProductList(
            @ApiParam(value = "상품 요청 DTO") @RequestBody ProductDTO.GetRequest productReqDTO
            ) {
        return productService.findAllSearch(productReqDTO);
    }

    /**
     * 상품 조회
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    public APIResponse getProductDetail(
            @ApiParam(value = "상품 요청 DTO")@PathVariable Long id) {
        Product product = productService.findById(id);
        //productDTO = productService.convertToDTO(id);
        return APIResponse.success("product",product);
    }
}
