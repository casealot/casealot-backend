package kr.casealot.shop.domain.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.service.S3UploadService;
import kr.casealot.shop.domain.file.service.UploadFileService;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cal/v1/file")
@Api(tags = {"S3 파일 업로드 API"})
@RequiredArgsConstructor
public class FileUploadController {
    private final ProductService productService;
    private final S3UploadService s3UploadService;
    private final UploadFileService uploadFileService;

    @Value("${cloud.aws.url}")
    private String awsUrl;

    @PostMapping("/{product_id}/image")
    @ApiOperation(value = "상품 이미지 업로드", notes = "상품의 이미지와 썸네일 이미지를 업로드한다.")
    public APIResponse uploadImage(
            @ApiParam(value = "상품 ID") @PathVariable("product_id") Long id,
            @ApiParam(value = "상품 썸네일 이미지") @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnailFile,
            @ApiParam(value = "상품 여러 이미지") @RequestParam(value = "images", required = false) List<MultipartFile> imagesFiles
    ) throws Exception {

        UploadFile thumbnail = null;
        if(null != thumbnailFile){
            String path = s3UploadService.uploadFile(thumbnailFile);
            thumbnail = uploadFileService.create(UploadFile.builder()
                            .name(thumbnailFile.getOriginalFilename())
                            .url(awsUrl + path)
                            .fileType(thumbnailFile.getContentType())
                            .fileSize(thumbnailFile.getSize())
                    .build());
        }

        List<UploadFile> images = null;
        if(null != imagesFiles){
            for(MultipartFile imageFile : imagesFiles){
                String path = s3UploadService.uploadFile(imageFile);
                images.add(uploadFileService.create(UploadFile.builder()
                        .name(thumbnailFile.getOriginalFilename())
                        .url(awsUrl + path)
                        .fileType(thumbnailFile.getContentType())
                        .fileSize(thumbnailFile.getSize())
                        .build()));
            }
        }

        return productService.saveProductWithImage(id, thumbnail, images);
    }
}