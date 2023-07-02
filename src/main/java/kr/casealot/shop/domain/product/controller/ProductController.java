package kr.casealot.shop.domain.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"PRODUCT API"}, description = "상품 조회(검색), 상세 조회")
@RequestMapping("/cal/v1/product")
public class ProductController {

    private final ProductService productService;

    /**
     * 전체 상품 조회
     *
     * @return
     */
    @PostMapping
    @ApiOperation(value = "상품 검색 및 조회", notes = "상품 정보를 갖고온다.")
    public APIResponse<ProductDTO.GetResponse> getProductList(
            @ApiParam(value = "상품 요청 DTO") @RequestBody ProductDTO.GetRequest productReqDTO
    ) {
        return productService.search(productReqDTO);
    }

    /**
     * 상품 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public APIResponse<ProductDTO.DetailResponse> getProductDetail(
            @ApiParam(value = "상품 요청 DTO") @PathVariable Long id, Principal principal) throws Exception {
        return productService.getDetailProduct(id, principal);
    }

    @GetMapping("/type/{type}")
    public APIResponse<ProductDTO.GetResponse> getProductByType(
            @PathVariable String type) {

        return productService.getProductByType(type);
    }

    @GetMapping("/category/{category}")
    public APIResponse<ProductDTO.GetResponse> getProductByCategory(
            @PathVariable String category) {

        return productService.getProductByCategory(category);
    }
}
