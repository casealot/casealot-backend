package kr.casealot.shop.domain.product.service;

import kr.casealot.shop.domain.product.dto.ProductReqDTO;
import kr.casealot.shop.domain.product.dto.ProductResDTO;
import kr.casealot.shop.domain.product.dto.SortDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.support.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product update(Product product) {
        return productRepository.save(product);
    }

    public Product delete(Product product) {
        return productRepository.save(product);
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
