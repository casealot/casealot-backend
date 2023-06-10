package kr.casealot.shop.domain.wishlist.wishlistItem.repository;

import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    WishlistItem findByProductAndWishlist(Product product, Wishlist wishlist);

    void deleteByProductAndWishlist(Product product, Wishlist wishlist);

}
