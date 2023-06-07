package kr.casealot.shop.domain.cart.repository;

import kr.casealot.shop.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(String customerId);
}