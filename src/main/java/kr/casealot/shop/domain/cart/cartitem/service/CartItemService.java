package kr.casealot.shop.domain.cart.cartitem.service;

import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.cart.cartitem.repository.CartItemRepository;
import kr.casealot.shop.domain.cart.dto.CartResDTO;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final String API_NAME = "cart";
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public APIResponse<List<CartResDTO>> reduceCartItemQuantity(Principal principal, Long cartItemId) {
        Customer customer = customerRepository.findCustomerById(principal.getName());
        if (customer == null) {
            return APIResponse.nullCheckPlease();
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            return APIResponse.nullCheckPlease();
        }

        CartItem cartItem = getCartItemFromCart(cart, cartItemId);
        if (cartItem == null) {
            return APIResponse.nullCheckPlease();
        }

        int currentQuantity = cartItem.getQuantity();
        if (currentQuantity <= 1) {
            removeCartItem(principal, cartItemId);
        } else {
            cartItem.setQuantity(currentQuantity - 1);
            cartItemRepository.save(cartItem);
        }

        List<CartResDTO> cartResDTOList = getCartResDtoList(cart.getSeq());

        return APIResponse.success(API_NAME, cartResDTOList);
    }

    @Transactional
    public APIResponse<List<CartResDTO>> addCartItemQuantity(Principal principal, Long cartItemId) {
        Customer customer = customerRepository.findCustomerById(principal.getName());
        if (customer == null) {
            return APIResponse.nullCheckPlease();
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            return APIResponse.nullCheckPlease();
        }

        CartItem cartItem = getCartItemFromCart(cart, cartItemId);
        if (cartItem == null) {
            return APIResponse.nullCheckPlease();
        }

        int currentQuantity = cartItem.getQuantity();

        cartItem.setQuantity(currentQuantity + 1);
        cartItemRepository.save(cartItem);

        List<CartResDTO> cartResDTOList = getCartResDtoList(cart.getSeq());

        return APIResponse.success(API_NAME, cartResDTOList);
    }

    @Transactional
    public APIResponse<List<CartResDTO>> removeCartItem(Principal principal, Long cartItemId) {
        Customer customer = customerRepository.findCustomerById(principal.getName());
        if (customer == null) {
            log.warn("customer is null");
            return APIResponse.nullCheckPlease();
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            log.warn("cart is null");
            return APIResponse.nullCheckPlease();
        }

        CartItem cartItem = getCartItemFromCart(cart, cartItemId);
        if (cartItem == null) {
            log.warn("cartItem is null");
            return APIResponse.nullCheckPlease();
        }

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        List<CartResDTO> cartResDTOList = getCartResDtoList(cart.getSeq());

        return APIResponse.success(API_NAME, cartResDTOList);
    }


    private List<CartResDTO> getCartResDtoList(Long cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return cartItems.stream()
                .map(cartItem -> new CartResDTO(cartId, cartItem.getProduct().getName(), cartItem.getQuantity()))
//                .map(cartItem -> new CartResDto(cartId, cartItem.getSeq(), cartItem.getProduct().getName(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private CartItem getCartItemFromCart(Cart cart, Long cartItemId) {
        if (cart == null || cart.getCartItems() == null) {
            return null;
        }

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProduct().getId().equals(cartItemId)) {
                return cartItem;
            }
        }

        return null;
    }
}
