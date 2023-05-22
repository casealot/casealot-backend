package kr.casealot.shop.domain.product.service;

import kr.casealot.shop.domain.product.dto.ProductListDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product delete(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public ProductListDTO findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        ProductListDTO productListDTO = ProductListDTO.builder()
                .items(products.getContent())
                .count((long) products.getContent().size())
                .totalCount(products.getTotalElements())
                .totalPages((long) products.getTotalPages()).build();
        return productListDTO;
    }


}
