package kr.casealot.shop.domain.product.controller;

import kr.casealot.shop.domain.product.dto.ProductDTO;
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

import static org.springframework.data.crossstore.ChangeSetPersister.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/product")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO){
        productService.createProduct(productDTO);
        return ResponseEntity.ok("create product");
    }

    @PutMapping("/admin/product/{product_id}")
    public ResponseEntity<String> updateProduct(@PathVariable("product_id") Long productId,
                                                @RequestBody ProductDTO productDTO){

        productService.updateProduct(productId, productDTO);

        return ResponseEntity.ok("update product");
    }

    @DeleteMapping("/admin/product/{product_id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("product_id") Long productId) throws NotFoundException {

        productService.deleteProduct(productId);
        return ResponseEntity.ok("delete product");
    }




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
