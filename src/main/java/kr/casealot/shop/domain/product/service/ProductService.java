package kr.casealot.shop.domain.product.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.service.S3UploadService;
import kr.casealot.shop.domain.file.service.UploadFileService;
import kr.casealot.shop.domain.product.dto.*;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.repository.ReviewCommentRepository;
import kr.casealot.shop.domain.product.support.ProductSpecification;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;
import static org.springframework.data.crossstore.ChangeSetPersister.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final String API_NAME = "product";

    private final S3UploadService s3UploadService;
    private final UploadFileService uploadFileService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AuthTokenProvider authTokenProvider;
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

//    @Transactional
//    public void updateProduct(Long productId, ProductDTO productDTO,
//                              HttpServletRequest request) throws NotFoundException {
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(NotFoundException::new);
//
//        String customerId = findCustomerId(request);
//
//        boolean isAdmin = checkAdminRole(customerId);
//
//        if (!isAdmin) {
//            throw new AccessDeniedException("You are not authorized to update this Product.");
//        }
//
//        product.setName(productDTO.getName());
//        product.setContent(productDTO.getContent());
//        product.setImg_B(productDTO.getImg_B());
//        product.setImg_M(productDTO.getImg_M());
//        product.setImg_S(productDTO.getImg_S());
//        product.setPrice(productDTO.getPrice());
//        product.setSale(productDTO.getSale());
//        product.setColor(productDTO.getColor());
//        product.setSeason(productDTO.getSeason());
//        product.setType(productDTO.getType());
//
//        productRepository.save(product);
//    }

    @Transactional
    public APIResponse deleteProduct(Long productId, HttpServletRequest request) throws NotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);

        // TODO UUID로 상품이미지 제거 필요
        // String customerId = findCustomerId(request);
        //        boolean isAdmin = checkAdminRole(customerId);
        //
        //        if (!isAdmin) {
        //            throw new AccessDeniedException("You are not authorized to delete this product.");
        //        }
        productRepository.delete(product);
        return APIResponse.success(API_NAME, productId);
    }

    public APIResponse findAllSearch(ProductDTO.GetRequest productReqDTO) {

        // query
        String query = productReqDTO.getQuery();

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

    public Product findById(Long id) {
        Product savedProduct = productRepository.findById(id).orElseGet(Product::new);
        // 상품 조회시 상품 조회 수 증가.
        savedProduct.setViews(savedProduct.getViews() + 1);
        productRepository.save(savedProduct);
        return savedProduct;
    }

//    public ProductGetDTO convertToDTO(Product product) {
//        List<ReviewResDTO> reviewList = new ArrayList<>();
//        for (Review review : product.getReviews()) {
//            List<ReviewCommentResDTO> reviewCommentList = new ArrayList<>();
//            for (ReviewComment reviewComment : review.getReviewCommentList()) {
//                ReviewCommentResDTO reviewCommentDTO = ReviewCommentResDTO.builder()
//                        .customerName(reviewComment.getCustomer().getName())
//                        .reviewCommentText(reviewComment.getReviewCommentText())
//                        .build();
//                reviewCommentList.add(reviewCommentDTO);
//            }
//            ReviewResDTO reviewDTO = ReviewResDTO.builder()
//                    .customerName(review.getCustomer().getName())
//                    .rating(review.getRating())
//                    .reviewText(review.getReviewText())
//                    .reviewCommentList(reviewCommentList)
//                    .build();
//            reviewList.add(reviewDTO);
//        }
//
//        return ProductGetDTO.builder()
//                .id(product.getId())
//                .userId(product.getId())
//                .name(product.getName())
//                .content(product.getContent())
//                .price(product.getPrice())
//                .views(product.getViews())
//                .img_B(product.getImg_B())
//                .img_M(product.getImg_M())
//                .img_S(product.getImg_S())
//                .sells(product.getSells())
//                .sale(product.getSale())
//                .color(product.getColor())
//                .season(product.getSeason())
//                .type(product.getType())
//                .reviewList(reviewList)
//                .build();
//    }


    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }

    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }

    @Transactional
    public APIResponse createProduct(
            ProductDTO.CreateRequest createRequest) {

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
    }

    @Transactional
    public APIResponse saveProductWithImage(Long id, UploadFile thumbnail, List<UploadFile> images) throws Exception {
        Product savedProduct = productRepository.findById(id).orElseThrow(() -> new Exception("상품이 존재하지 않습니다."));

        savedProduct.setThumbnail(thumbnail);
        savedProduct.setImages(images);

        savedProduct = productRepository.saveAndFlush(savedProduct);
        return APIResponse.success(API_NAME, savedProduct);
    }
}
