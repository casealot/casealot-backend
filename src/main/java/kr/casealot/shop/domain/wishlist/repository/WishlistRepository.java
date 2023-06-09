package kr.casealot.shop.domain.wishlist.repository;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByCustomerId(String id);


    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist = :wishlist")
    void deleteCartItemsByCart(@Param("wishlist") Wishlist wishlist);
}
