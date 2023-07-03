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
    @ApiOperation(value = "상품 상세 조회", notes = "상품의 상세 정보 조회")
    public APIResponse<ProductDTO.DetailResponse> getProductDetail(
            @ApiParam(value = "상품 요청 DTO") @PathVariable Long id, Principal principal) throws Exception {
        return productService.getDetailProduct(id, principal);
    }

    @PostMapping("/type/{type}")
    @ApiOperation(value = "타입별 상품 목록 조회", notes = "EX) NEW, BEST")
    public APIResponse<ProductDTO.GetResponse> getProductByType(
            @ApiParam(value = "타입명") @PathVariable("type") String type,
            @ApiParam(value = "상품 요청 DTO") @RequestBody ProductDTO.GetRequest productReqDTO) {

        return productService.getProductByType(type, productReqDTO);
    }

    @PostMapping("/category/{category}")
    @ApiOperation(value = "카테고리별 상품 목록 조회", notes = "EX) CAP, TOP, ACC")
    public APIResponse<ProductDTO.GetResponse> getProductByCategory(
            @ApiParam(value = "카테고리명") @PathVariable("category") String category,
            @ApiParam(value = "상품 요청 DTO") @RequestBody ProductDTO.GetRequest productReqDTO) {

        return productService.getProductByCategory(category, productReqDTO);
    }
}
