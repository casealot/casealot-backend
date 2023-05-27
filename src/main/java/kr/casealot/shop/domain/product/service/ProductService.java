package kr.casealot.shop.domain.product.service;

import kr.casealot.shop.domain.product.dto.ProductResDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public ProductResDTO findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        ProductResDTO productResDTO = ProductResDTO.builder()
                .items(products.getContent())
                .count((long) products.getContent().size())
                .totalCount(products.getTotalElements())
                .totalPages((long) products.getTotalPages()).build();
        return productResDTO;
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseGet(Product::new);
    }
}
