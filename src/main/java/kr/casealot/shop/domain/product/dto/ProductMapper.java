package kr.casealot.shop.domain.product.dto;

import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.global.exception.NotFoundProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductRepository productRepository;
    public Product createRequestDTOToEntity(ProductDTO.Request request){
        Product saveProduct = Product.builder()
                .name(request.getName())
                .content(request.getContent())
                .price(request.getPrice())
                .sale(request.getSale())
                .color(request.getColor())
                .season(request.getSeason())
                .type(request.getType())
                .build();
        return saveProduct;
    }

    public Product updateRequestDTOToEntity(Long id, ProductDTO.Request request) {
        Product savedProduct = productRepository.findById(id)
                .orElseThrow(NotFoundProductException::new);

        Product updateProduct = Product.builder()
                .id(id)
                .name(request.getName())
                .content(request.getContent())
                .price(request.getPrice())
                .sale(request.getSale())
                .color(request.getColor())
                .season(request.getSeason())
                .type(request.getType())
                .thumbnail(savedProduct.getThumbnail())
                .images(savedProduct.getImages())
                .build();
        return updateProduct;
    }
}
