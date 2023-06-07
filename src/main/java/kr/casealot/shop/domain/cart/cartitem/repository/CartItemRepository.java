package kr.casealot.shop.domain.cart.cartitem.repository;

import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), item.getId());
    CartItem findByCart(Long cartSeq, Long productSeq);
}
