package kr.casealot.shop.domain.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.service.S3UploadService;
import kr.casealot.shop.domain.file.service.UploadFileService;
import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/cal/v1/file")
@Api(tags = {"S3 파일 업로드 API"})
@RequiredArgsConstructor
public class FileUploadController {
    private final ProductService productService ;
    private final CustomerService customerService ;

    @PostMapping("/{product_id}/image")
    @ApiOperation(value = "상품 이미지 업로드", notes = "상품의 이미지와 썸네일 이미지를 업로드한다.")
    public APIResponse uploadImage(
            @ApiParam(value = "상품 ID") @PathVariable("product_id") Long id,
            @ApiParam(value = "상품 썸네일 이미지") @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnailFile,
            @ApiParam(value = "상품 여러 이미지") @RequestParam(value = "images", required = false) List<MultipartFile> imagesFiles
    ) throws Exception {
        return productService.saveProductWithImage(id, thumbnailFile, imagesFiles);
    }

    @PutMapping("/{product_id}/image")
    @ApiOperation(value = "상품 이미지 업로드 수정", notes = "기존에 등록되어있는 이미지는 삭제하고 새로 이미지를 업로드한다.")
    public APIResponse modifyUploadedImage(
            @ApiParam(value = "상품 ID") @PathVariable("product_id") Long id,
            @ApiParam(value = "상품 썸네일 이미지") @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnailFile,
            @ApiParam(value = "상품 여러 이미지") @RequestParam(value = "images", required = false) List<MultipartFile> imagesFiles
    ) throws Exception {
        return productService.modifyProductWithImage(id, thumbnailFile, imagesFiles);
    }

    @PutMapping("/{customer_id}/customer/image")
    @ApiOperation(value = "회원 프로필 이미지 업로드 수정", notes = "기존에 등록되어있는 이미지는 삭제하고 새로 이미지를 업로드한다.")
    public APIResponse<Customer> modifyUploadedCustomerImage(
        @PathVariable("customer_id") String id,
        MultipartFile profileFile
    ) throws Exception {
        return customerService.modifyProfileWithImage(id, profileFile);
    }

}
