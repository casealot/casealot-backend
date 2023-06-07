package kr.casealot.shop.domain.cart.service;

import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.cart.cartitem.repository.CartItemRepository;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.service.ProductService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CustomerRepository customerRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public APIResponse<Cart> addItemToCart(Principal principal, Long productId, int quantity) {
        Customer customer = customerRepository.findById(principal.getName());
        Product product = productRepository.findById(productId).orElse(null);

        if (customer == null || product == null) {
            // 필요한 예외 처리
            return APIResponse.permissionDenied();
        }

        Cart cart = cartRepository.findByCustomerId(customer.getSeq());

        if (cart == null) {
            cart = Cart.createCart(customer);
        }

        CartItem cartItem = cart.getCartItemByProduct(product);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cart.setQuantity(cart.getQuantity() + quantity);

        cartRepository.save(cart);
        return APIResponse.success("cart",cart);
    }
}
