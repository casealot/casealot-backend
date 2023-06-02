package kr.casealot.shop.domain.product.controller;

import kr.casealot.shop.domain.product.dto.ProductDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.product.dto.ProductGetDTO;
import kr.casealot.shop.domain.product.dto.ProductResDTO;
import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.data.crossstore.ChangeSetPersister.*;

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
    public ResponseEntity<ProductGetDTO> getProductDetail(
            @ApiParam(value = "상품 요청 DTO")@PathVariable Long id) {
        Product product = productService.findById(id);
        ProductGetDTO productDTO = productService.convertToDTO(product);
        return ResponseEntity.ok(productDTO);
    }


}
