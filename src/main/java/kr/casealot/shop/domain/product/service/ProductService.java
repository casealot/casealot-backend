package kr.casealot.shop.domain.product.service;

import kr.casealot.shop.domain.product.dto.ProductDTO;
import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.dto.ProductResDTO;
import kr.casealot.shop.domain.product.dto.SortDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.support.ProductSpecification;
import kr.casealot.shop.domain.qna.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void createProduct(ProductDTO productDTO) {
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
                .build();

        productRepository.save(product);
    }


    @Transactional
    public void updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow();

        product.setName(product.getName());
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


    public void deleteProduct(Long productId) throws NotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);

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

}
