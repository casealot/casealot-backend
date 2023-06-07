package kr.casealot.shop.domain.cart.cartitem.service;

import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.cart.cartitem.repository.CartItemRepository;
import kr.casealot.shop.domain.cart.dto.CartResDto;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;

    // Reduce the quantity of a cart item
    @Transactional
    public APIResponse<List<CartResDto>> reduceCartItemQuantity(Principal principal, Long cartItemId, int quantity) {
        Customer customer =  customerRepository.findCustomerById(principal.getName());
        if (customer == null) {
            // Handle customer not found exception
            return APIResponse.fail();
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            // Handle cart not found exception
            return APIResponse.fail();
        }

        CartItem cartItem = getCartItemFromCart(cart, cartItemId);
        if (cartItem == null) {
            // Handle cart item not found exception
            return APIResponse.fail();
        }

        int currentQuantity = cartItem.getQuantity();
        if (currentQuantity <= quantity) {
            // Remove the cart item if the requested quantity is greater than or equal to the current quantity
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(currentQuantity - quantity);
            cartItemRepository.save(cartItem);
        }

        // Retrieve the updated cart items after modification
        List<CartResDto> cartResDtoList = getCartResDtoList(cart.getSeq());

        return APIResponse.success("cart", cartResDtoList);
    }

    @Transactional
    public APIResponse<List<CartResDto>> removeCartItem(Principal principal, Long cartItemId) {
        Customer customer =  customerRepository.findCustomerById(principal.getName());
        if (customer == null) {
            // Handle customer not found exception
            return APIResponse.fail();
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            // Handle cart not found exception
            return APIResponse.fail();
        }

        CartItem cartItem = getCartItemFromCart(cart, cartItemId);
        if (cartItem == null) {
            // Handle cart item not found exception
            return APIResponse.fail();
        }

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        // Retrieve the updated cart items after removal
        List<CartResDto> cartResDtoList = getCartResDtoList(cart.getSeq());

        return APIResponse.success("cart", cartResDtoList);
    }



    // Retrieve the cart items and return a list of CartResDto
    private List<CartResDto> getCartResDtoList(Long cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return cartItems.stream()
                .map(cartItem -> new CartResDto(cartItem.getProduct().getName(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    // Helper method to retrieve a specific cart item from the cart
    private CartItem getCartItemFromCart(Cart cart, Long cartItemId) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getSeq().equals(cartItemId))
                .findFirst()
                .orElse(null);
    }
}
