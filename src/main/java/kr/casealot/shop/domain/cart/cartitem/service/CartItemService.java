package kr.casealot.shop.domain.cart.cartitem.service;

import static kr.casealot.shop.domain.cart.dto.CartGetDTO.buildCartGetDTO;

import com.amazonaws.services.kms.model.NotFoundException;
import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.cart.cartitem.repository.CartItemRepository;
import kr.casealot.shop.domain.cart.dto.CartGetDTO;
import kr.casealot.shop.domain.cart.dto.CartResDTO;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.dto.ProductCartDTO;
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
  public APIResponse<CartGetDTO> reduceCartItemQuantity(Principal principal, Long cartItemId) {
    Customer customer = customerRepository.findCustomerById(principal.getName());
    if (customer == null) {
      throw new NotFoundException("존재하지 않는 고객에 대한 요청입니다.");
    }

    Cart cart = cartRepository.findByCustomerId(customer.getId());
    if (cart == null) {
      throw new NotFoundException("존재하지 않는 장바구니에 대한 요청입니다.");
    }

    CartItem cartItem = getCartItemFromCart(cart, cartItemId);
    if (cartItem == null) {
      throw new NotFoundException("존재하지 않는 상품에 대한 요청입니다.");
    }

    int currentQuantity = cartItem.getQuantity();
    if (currentQuantity <= 1) {
      removeCartItem(principal, cartItemId);
    } else {
      cartItem.setQuantity(currentQuantity - 1);
      cartItemRepository.save(cartItem);
    }

    CartGetDTO cartGetDto = buildCartGetDTO(customer, cart, cart.getCartItems());

    return APIResponse.success(API_NAME, cartGetDto);
  }

  @Transactional
  public APIResponse<CartGetDTO> addCartItemQuantity(Principal principal, Long cartItemId) {
    Customer customer = customerRepository.findCustomerById(principal.getName());
    if (customer == null) {
      throw new NotFoundException("존재하지 않는 고객에 대한 요청입니다.");
    }

    Cart cart = cartRepository.findByCustomerId(customer.getId());
    if (cart == null) {
      throw new NotFoundException("존재하지 않는 장바구니에 대한 요청입니다.");
    }

    CartItem cartItem = getCartItemFromCart(cart, cartItemId);
    if (cartItem == null) {
      throw new NotFoundException("존재하지 않는 상품에 대한 요청입니다.");
    }

    int currentQuantity = cartItem.getQuantity();

    cartItem.setQuantity(currentQuantity + 1);
    cartItemRepository.save(cartItem);
    CartGetDTO cartGetDto = buildCartGetDTO(customer, cart, cart.getCartItems());

    return APIResponse.success(API_NAME, cartGetDto);
  }

  @Transactional
  public APIResponse<CartGetDTO> removeCartItem(Principal principal, Long cartItemId) {
    Customer customer = customerRepository.findCustomerById(principal.getName());
    if (customer == null) {
      throw new NotFoundException("존재하지 않는 고객에 대한 요청입니다.");
    }

    Cart cart = cartRepository.findByCustomerId(customer.getId());
    if (cart == null) {
      throw new NotFoundException("존재하지 않는 장바구니에 대한 요청입니다.");
    }

    CartItem cartItem = getCartItemFromCart(cart, cartItemId);
    if (cartItem == null) {
      throw new NotFoundException("존재하지 않는 상품에 대한 요청입니다.");
    }

    cart.getCartItems().remove(cartItem);
    cartItemRepository.delete(cartItem);
    CartGetDTO cartGetDto = buildCartGetDTO(customer, cart, cart.getCartItems());

    return APIResponse.success(API_NAME, cartGetDto);
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
