package kr.casealot.shop.domain.product.dto;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.wishlist.wishlistItem.repository.WishlistItemRepository;
import kr.casealot.shop.global.exception.NotFoundProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

  private final ProductRepository productRepository;
  private final WishlistItemRepository wishlistItemRepository;
  private final CustomerRepository customerRepository;

  public Product createRequestDTOToEntity(ProductDTO.Request request) {
    Product saveProduct = Product.builder()
        .name(request.getName())
        .content(request.getContent())
        .price(request.getPrice())
        .calculatePrice(request.getPrice() * (100 - request.getSale()) * 0.01)
        .sale(request.getSale())
        .color(request.getColor())
        .category(request.getCategory())
        .type("new")
        .sells(0)
        .rating(0.0)
        .ratingCount(0.0)
        .totalRating(0.0)
        .wishCount(0)
        .season(request.getSeason())
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
        .calculatePrice(request.getPrice() * (100 - request.getSale()) * 0.01)
        .sale(request.getSale())
        .color(request.getColor())
        .season(request.getSeason())
        .category(request.getCategory())
        .type(savedProduct.getType())
        .rating(savedProduct.getRating())
        .sells(savedProduct.getSells())
        .ratingCount(savedProduct.getRatingCount())
        .totalRating(savedProduct.getTotalRating())
        .wishCount(savedProduct.getWishCount())
        .thumbnail(savedProduct.getThumbnail())
        .images(savedProduct.getImages())
        .build();
    return updateProduct;
  }

  public List<ProductDTO.ProductInfo> convertEntityToDTOS(List<Product> products) {
    List<ProductDTO.ProductInfo> productInfos = new ArrayList<>();
    for (Product product : products) {
//      int wishCount = wishlistItemRepository.countByProduct_Id(product.getId());
      productInfos.add(ProductDTO.ProductInfo.builder()
          .id(product.getId())
          .name(product.getName())
          .content(product.getContent())
          .price(product.getPrice())
          .category(product.getCategory())
          .calculatePrice(product.getPrice() * (100 - product.getSale()) * 0.01)
          .sale(product.getSale())
          .thumbnail(product.getThumbnail())
          .sells(product.getSells())
          .rating(product.getRating())
          .ratingCount(product.getRatingCount())
          .color(product.getColor())
          .type(product.getType())
          .wishCount(product.getWishCount())
          .wishYn("N")
          .createdDt(product.getCreatedDt())
          .modifiedDt(product.getModifiedDt())
          .build());
    }
    return productInfos;
  }

  public ProductDTO.ProductInfo convertEntityToDTO(Product product) {
    return convertEntityToDTO(product, null);
  }

  public ProductDTO.ProductInfo convertEntityToDTO(Product product, Principal principal) {
    String wishYn = "N";
    if (principal != null) {
      Customer customer = customerRepository.findById(principal.getName());
      int wishCount = wishlistItemRepository.countByProductIdAndWishlistId(product.getId(),
          customer.getSeq());
      if (wishCount > 0) {
        wishYn = "Y";
      }
    }
//    int wishCount = wishlistItemRepository.countByProduct_Id(product.getId());
    return ProductDTO.ProductInfo.builder()
        .id(product.getId())
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .calculatePrice(product.getPrice() * (100 - product.getSale()) * 0.01)
        .sale(product.getSale())
        .color(product.getColor())
        .thumbnail(product.getThumbnail())
        .rating(product.getRating())
        .ratingCount(product.getRatingCount())
        .type(product.getType())
        .sells(product.getSells())
        .wishCount(product.getWishCount())
        .wishYn(wishYn)
        .createdDt(product.getCreatedDt())
        .modifiedDt(product.getModifiedDt())
        .category(product.getCategory())
        .build();
  }
}
