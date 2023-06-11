package kr.casealot.shop.domain.wishlist.wishlistItem.repository;

import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    void deleteByProductAndWishlist(Product product, Wishlist wishlist);
    List<WishlistItem> findByProduct(Product product);
    int countByProduct_Id(Long id);

    @Query("SELECT COUNT(*) FROM WishlistItem w INNER JOIN Wishlist wl ON wl.id = w.wishlist.id WHERE w.product.id = :productId AND wl.customer.seq = :customerSeq")
    int countByProductIdAndWishlistId(@Param("productId") Long productId, @Param("customerSeq") Long customerSeq);
}
