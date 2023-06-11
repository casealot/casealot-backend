package kr.casealot.shop.domain.product.dto;

import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.wishlist.wishlistItem.repository.WishlistItemRepository;
import kr.casealot.shop.global.exception.NotFoundProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductRepository productRepository;
    private final WishlistItemRepository wishlistItemRepository;

    public Product createRequestDTOToEntity(ProductDTO.Request request) {
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

    public List<ProductDTO.ProductInfo> convertEntityToDTOS(List<Product> products) {
        List<ProductDTO.ProductInfo> productInfos = new ArrayList<>();
        for (Product product : products) {
            int wishCount = wishlistItemRepository.countByProduct_Id(product.getId());
            productInfos.add(ProductDTO.ProductInfo.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .content(product.getContent())
                    .price(product.getPrice())
                    .sale(product.getSale())
                    .color(product.getColor())
                    .type(product.getType())
                    .wishCount(wishCount)
                    .createdDt(product.getCreatedDt())
                    .modifiedDt(product.getModifiedDt())
                    .build());
        }
        return productInfos;
    }

    public ProductDTO.ProductInfo convertEntityToDTO(Product product) {
        int wishCount = wishlistItemRepository.countByProduct_Id(product.getId());
        return ProductDTO.ProductInfo.builder()
                .id(product.getId())
                .name(product.getName())
                .content(product.getContent())
                .price(product.getPrice())
                .sale(product.getSale())
                .color(product.getColor())
                .type(product.getType())
                .wishCount(wishCount)
                .createdDt(product.getCreatedDt())
                .modifiedDt(product.getModifiedDt())
                .build();
    }
}
