package kr.casealot.shop.domain.cart.service;

import static kr.casealot.shop.domain.cart.dto.CartGetDTO.buildCartGetDTO;

import com.amazonaws.services.kms.model.NotFoundException;
import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.cart.dto.CartGetDTO;
import kr.casealot.shop.domain.cart.dto.CartResDTO;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.dto.ProductCartDTO;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.AlreadyDeletedException;
import kr.casealot.shop.global.exception.NotFoundProductException;
import kr.casealot.shop.global.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

  private final String API_NAME = "cart";

  private final CartRepository cartRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;

  @Transactional
  public APIResponse<CartGetDTO> addItemToCart(Principal principal, Long productId) {
    Customer customer = customerRepository.findById(principal.getName());
    Product product = productRepository.findById(productId).orElse(null);

    if (customer == null) {
      throw new NotFoundUserException();
    }
    if (product == null) {
      throw new NotFoundProductException();
    }

    Cart cart = cartRepository.findByCustomerId(principal.getName());
    if (cart == null) {
      cart = Cart.createCart(customer);
    }

    List<CartItem> cartItems = cart.getCartItems();
    CartItem cartItem = null;
    if (cartItems != null) {
      for (CartItem item : cartItems) {
        if (item.getProduct().equals(product)) {
          cartItem = item;
          break;
        }
      }
    }

    if (cartItem == null) {
      cartItem = new CartItem();
      cartItem.setProduct(product);
      cartItem.setQuantity(1);
      cartItem.setCart(cart);
      cart.getCartItems().add(cartItem);
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
    }

    cartRepository.save(cart);

    CartGetDTO cartGetDto = buildCartGetDTO(customer, cart, cart.getCartItems());

    return APIResponse.success(API_NAME, cartGetDto);
  }

  @Transactional
  public APIResponse<CartGetDTO> addManyItemToCart(Principal principal, Long productId,
      int quantity) {
    Customer customer = customerRepository.findById(principal.getName());
    Product product = productRepository.findById(productId).orElse(null);

    if (customer == null) {
      throw new NotFoundUserException();
    }
    if (product == null) {
      throw new NotFoundProductException();
    }

    Cart cart = cartRepository.findByCustomerId(principal.getName());
    if (cart == null) {
      cart = Cart.createCart(customer);
    }

    List<CartItem> cartItems = cart.getCartItems();
    CartItem cartItem = null;
    if (cartItems != null) {
      for (CartItem item : cartItems) {
        if (item.getProduct().equals(product)) {
          cartItem = item;
          break;
        }
      }
    }

    if (cartItem == null) {
      cartItem = new CartItem();
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setCart(cart);
      cart.getCartItems().add(cartItem);
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    cartRepository.save(cart);

    CartGetDTO cartGetDto = buildCartGetDTO(customer, cart, cart.getCartItems());

    return APIResponse.success(API_NAME, cartGetDto);
  }

  @Transactional
  public APIResponse<String> clearCart(Principal principal) {
    Customer customer = customerRepository.findCustomerById(principal.getName());
    if (customer == null) {
      throw new NotFoundUserException();
    }

    Cart cart = cartRepository.findByCustomerId(principal.getName());
    if (cart == null) {
      throw new AlreadyDeletedException();
    }

    cartRepository.deleteCartItemsByCart(cart); // 관련된 cart_item 삭제

    cartRepository.delete(cart); // cart 삭제

    return APIResponse.success(API_NAME, principal.getName() + " cart is clean");
  }

  @Transactional
  public APIResponse<CartGetDTO> getCart(Principal principal) {
    String customerId = principal.getName(); // 현재 사용자의 ID

    Customer customer = customerRepository.findCustomerById(customerId);
    if (customer == null) {
      throw new NotFoundUserException();
    }

    //카트 조회시 없으면 생성 후 db에 즉시 저장
    Cart cart = cartRepository.findByCustomerId(customer.getId());
    if (cart == null) {
      cart = Cart.createCart(customer);
      cartRepository.saveAndFlush(cart);
    }

    CartGetDTO cartGetDto = buildCartGetDTO(customer, cart, cart.getCartItems());

    return APIResponse.success(API_NAME, cartGetDto);
  }
}

