package kr.casealot.shop.domain.wishlist.repository;

import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByCustomerId(String id);
}
