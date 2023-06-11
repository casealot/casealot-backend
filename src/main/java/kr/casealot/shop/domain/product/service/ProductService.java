package kr.casealot.shop.domain.product.service;

import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.service.S3UploadService;
import kr.casealot.shop.domain.file.service.UploadFileService;
import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.dto.SortDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import kr.casealot.shop.domain.product.support.ProductSpecification;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.DuplicateEmailException;
import kr.casealot.shop.global.exception.DuplicateProductException;
import kr.casealot.shop.global.exception.NotFoundProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final String API_NAME = "product";
    private final S3UploadService s3UploadService;
    private final UploadFileService uploadFileService;
    private final ProductRepository productRepository;

    @Transactional
    public APIResponse<ProductDTO.GetResponse> findAllSearch(ProductDTO.GetRequest productReqDTO) {

        // criteria query
        Specification<Product> specification = new ProductSpecification(productReqDTO.getQuery(), productReqDTO.getFilter());

        // Sorting
        List<SortDTO> sortDTO = productReqDTO.getSort();
        List<Sort.Order> orders = new ArrayList<>();
        if (null != sortDTO) {
            for (SortDTO dto : sortDTO) {
                orders.add(new Sort.Order(Sort.Direction.fromString(dto.getOption()), dto.getField()));
            }
        }

        Pageable pageable = PageRequest.of(productReqDTO.getPage()
                , productReqDTO.getSize()
                , Sort.by(orders));

        Page<Product> products = productRepository.findAll(specification, pageable);

        ProductDTO.GetResponse response = ProductDTO.GetResponse.builder()
                .items(products.getContent())
                .count((long) products.getContent().size())
                .totalCount(products.getTotalElements())
                .totalPages((long) products.getTotalPages()).build();

        return APIResponse.success(API_NAME, response);
    }

    @Transactional
    public APIResponse<ProductDTO.DetailResponse> findById(Long id) throws Exception {
        Product savedProduct = productRepository.findById(id).orElseThrow(
                NotFoundProductException::new);

        // 상품 조회시 상품 조회 수 증가.
        savedProduct.addView(savedProduct.getViews());
        productRepository.save(savedProduct);

        // 리뷰 추가
        List<ReviewResDTO> reviewList = new ArrayList<>();
        for (Review review : savedProduct.getReviews()) {
            List<ReviewCommentResDTO> reviewCommentList = new ArrayList<>();
            for (ReviewComment reviewComment : review.getReviewCommentList()) {
                ReviewCommentResDTO reviewCommentDTO = ReviewCommentResDTO.builder()
                        .id(reviewComment.getSeq())
                        .customerName(reviewComment.getCustomer().getName())
                        .reviewCommentText(reviewComment.getReviewCommentText())
                        .createdDt(reviewComment.getCreatedDt())
                        .modifiedDt(reviewComment.getModifiedDt())
                        .build();
                reviewCommentList.add(reviewCommentDTO);
            }
            ReviewResDTO reviewDTO = ReviewResDTO.builder()
                    .id(review.getSeq())
                    .customerName(review.getCustomer().getName())
                    .rating(review.getRating())
                    .reviewText(review.getReviewText())
                    .reviewCommentList(reviewCommentList)
                    .createdDt(review.getCreatedDt())
                    .modifiedDt(review.getModifiedDt())
                    .build();
            reviewList.add(reviewDTO);
        }

        return APIResponse.success(ProductDTO.DetailResponse.builder()
                .product(savedProduct)
                .reviewList(reviewList)
                .build());
    }

    @Transactional
    public APIResponse<Product> createProduct(
            ProductDTO.Request createRequest) throws DuplicateEmailException, DuplicateProductException {
        if (productRepository.findByName(createRequest.getName()) == null) {
            Product saveProduct = Product.builder()
                    .name(createRequest.getName())
                    .content(createRequest.getContent())
                    .price(createRequest.getPrice())
                    .sale(createRequest.getSale())
                    .color(createRequest.getColor())
                    .season(createRequest.getSeason())
                    .type(createRequest.getType())
                    .build();
            Product savedProduct = productRepository.saveAndFlush(saveProduct);
            return APIResponse.success(API_NAME, savedProduct);
        }else{
            throw new DuplicateProductException();
        }
    }

    @Transactional
    public APIResponse<Product> saveProductWithImage(
            Long id, MultipartFile thumbnailFile, List<MultipartFile> imagesFiles) throws Exception {

        Product savedProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException());

        UploadFile thumbnail = null;
        if(null != thumbnailFile){
            String path = s3UploadService.uploadFile(thumbnailFile);
            thumbnail = uploadFileService.create(UploadFile.builder()
                    .name(thumbnailFile.getOriginalFilename())
                    .url(path)
                    .fileType(thumbnailFile.getContentType())
                    .fileSize(thumbnailFile.getSize())
                    .build());
            savedProduct.setThumbnail(thumbnail);
        }

        List<UploadFile> images = null;
        if(null != imagesFiles){
            images = new ArrayList<>();
            for(MultipartFile imageFile : imagesFiles){
                String path = s3UploadService.uploadFile(imageFile);
                images.add(uploadFileService.create(UploadFile.builder()
                        .name(thumbnailFile.getOriginalFilename())
                        .url(path)
                        .fileType(thumbnailFile.getContentType())
                        .fileSize(thumbnailFile.getSize())
                        .build()));
            }
            savedProduct.setImages(images);
        }else{

        }

        savedProduct = productRepository.saveAndFlush(savedProduct);

        return APIResponse.success(API_NAME, savedProduct);
    }

    @Transactional
    public APIResponse modifyProductWithImage(Long id, MultipartFile thumbnailFile,
                                              List<MultipartFile> imagesFiles) throws Exception {
        Product savedProduct = productRepository.findById(id)
                .orElseThrow(NotFoundProductException::new);

        UploadFile thumbnail = null;
        if(null != thumbnailFile){
            //기존에 이미지가 있다면 삭제
            UploadFile savedThumbnailFile = savedProduct.getThumbnail();
            if(null != savedThumbnailFile){
                s3UploadService.deleteFileFromS3Bucket(savedThumbnailFile.getUrl());
                uploadFileService.delete(savedThumbnailFile);
            }

            String path = s3UploadService.uploadFile(thumbnailFile);
            thumbnail = uploadFileService.create(UploadFile.builder()
                    .name(thumbnailFile.getOriginalFilename())
                    .url(path)
                    .fileType(thumbnailFile.getContentType())
                    .fileSize(thumbnailFile.getSize())
                    .build());
            savedProduct.setThumbnail(thumbnail);
        }

        List<UploadFile> images = null;
        if(null != imagesFiles){
            images = new ArrayList<>();
            //기존에 이미지가 있다면 삭제
            List<UploadFile> savedImagesFile = savedProduct.getImages();
            if(null != savedImagesFile){
                for(UploadFile savedImage : savedImagesFile){
                    s3UploadService.deleteFileFromS3Bucket(savedImage.getUrl());
                    uploadFileService.delete(savedImage);
                }
            }

            for(MultipartFile imageFile : imagesFiles){
                String path = s3UploadService.uploadFile(imageFile);
                images.add(uploadFileService.create(UploadFile.builder()
                        .name(thumbnailFile.getOriginalFilename())
                        .url(path)
                        .fileType(thumbnailFile.getContentType())
                        .fileSize(thumbnailFile.getSize())
                        .build()));
            }
            savedProduct.setImages(images);
        }

        savedProduct = productRepository.saveAndFlush(savedProduct);

        return APIResponse.success(API_NAME, savedProduct);
    }

    @Transactional
    public APIResponse deleteProduct(Long productId) throws Exception {

        Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundProductException::new);

        // step1 : S3 업로드된 이미지 삭제
        // step2 : DB에 저장된 이미지 메타정보 삭제
        UploadFile thumbnailFile = product.getThumbnail();
        if (Objects.nonNull(thumbnailFile)) {
            s3UploadService.deleteFileFromS3Bucket(thumbnailFile.getUrl());
            uploadFileService.delete(thumbnailFile);
        }

        List<UploadFile> imagesFiles = product.getImages();
        if (Objects.nonNull(imagesFiles)) {
            for (UploadFile image : imagesFiles) {
                s3UploadService.deleteFileFromS3Bucket(image.getUrl());
                uploadFileService.delete(image);
            }
        }

        productRepository.delete(product);
        return APIResponse.delete();
    }

    @Transactional
    public APIResponse<Product> updateProduct(Long productId, ProductDTO.Request updateRequest) throws Exception {

        Product savedProduct = productRepository.findById(productId)
                .orElseThrow(NotFoundProductException::new);

        Product updateProduct = Product.builder()
                .id(productId)
                .name(updateRequest.getName())
                .content(updateRequest.getContent())
                .price(updateRequest.getPrice())
                .sale(updateRequest.getSale())
                .color(updateRequest.getColor())
                .season(updateRequest.getSeason())
                .type(updateRequest.getType())
                // 저장 되어 있는 썸네일 이미지 사용
                .thumbnail(savedProduct.getThumbnail())
                .images(savedProduct.getImages())
                .build();

        Product updatedProduct = productRepository.saveAndFlush(updateProduct);
        return APIResponse.success(API_NAME, updatedProduct);
    }

}
