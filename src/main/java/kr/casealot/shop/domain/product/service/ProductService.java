package kr.casealot.shop.domain.product.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.dto.ProductResDTO;
import kr.casealot.shop.domain.product.dto.SortDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.support.ProductSpecification;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AuthTokenProvider authTokenProvider;

    @Transactional
    public void createProduct(ProductDTO productDTO, HttpServletRequest request) {

        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        Customer customer = customerRepository.findById(customerId);

        Product product = Product.builder()
                .name(productDTO.getName())
                .content(productDTO.getContent())
                .price(productDTO.getPrice())
                .views(productDTO.getViews())
                .img_B(productDTO.getImg_B())
                .img_M(productDTO.getImg_M())
                .img_S(productDTO.getImg_S())
                .sells(productDTO.getSells())
                .sale(productDTO.getSale())
                .sells(productDTO.getSells())
                .color(productDTO.getColor())
                .season(productDTO.getSeason())
                .type(productDTO.getType())
                .customer(customer)
                .build();

        productRepository.save(product);
    }


    @Transactional
    public void updateProduct(Long productId, ProductDTO productDTO,
                              HttpServletRequest request) throws NotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not authorized to update this Product.");
        }

        product.setName(productDTO.getName());
        product.setContent(productDTO.getContent());
        product.setImg_B(productDTO.getImg_B());
        product.setImg_M(productDTO.getImg_M());
        product.setImg_S(productDTO.getImg_S());
        product.setPrice(productDTO.getPrice());
        product.setSale(productDTO.getSale());
        product.setColor(productDTO.getColor());
        product.setSeason(productDTO.getSeason());
        product.setType(productDTO.getType());

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId, HttpServletRequest request) throws NotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not authorized to delete this product.");
        }

        productRepository.delete(product);
    }

    public ProductResDTO findAllSearch(ProductReqDTO productReqDTO) {

        // query
        String query = productReqDTO.getQuery();

        // criteria query
        Specification<Product> specification = new ProductSpecification(productReqDTO.getQuery() ,productReqDTO.getFilter());

        // Sorting
        List<SortDTO> sortDTO = productReqDTO.getSort();
        List<Sort.Order> orders = new ArrayList<>();
        if(null != sortDTO){
            for(SortDTO dto : sortDTO){
                orders.add(new Sort.Order(Sort.Direction.fromString(dto.getOption()),dto.getField()));
            }
        }

        Pageable pageable = PageRequest.of(productReqDTO.getPage()
                , productReqDTO.getSize()
                , Sort.by(orders));

        Page<Product> products = productRepository.findAll(specification, pageable);
        ProductResDTO productResDTO = ProductResDTO.builder()
                .items(products.getContent())
                .count((long) products.getContent().size())
                .totalCount(products.getTotalElements())
                .totalPages((long) products.getTotalPages()).build();
        return productResDTO;
    }

    public Product findById(Long id) {
        Product savedProduct = productRepository.findById(id).orElseGet(Product::new);
        // 상품 조회시 상품 조회 수 증가.
        savedProduct.setViews(savedProduct.getViews() + 1);
        productRepository.save(savedProduct);
        return savedProduct;
    }

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

}
