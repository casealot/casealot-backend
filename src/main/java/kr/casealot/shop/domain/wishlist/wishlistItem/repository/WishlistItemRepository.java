package kr.casealot.shop.domain.wishlist.wishlistItem.repository;

import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
}
