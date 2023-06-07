//package kr.casealot.shop.domain.cart.service;
//
//import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
//import kr.casealot.shop.domain.cart.cartitem.repository.CartItemRepository;
//import kr.casealot.shop.domain.cart.entity.Cart;
//import kr.casealot.shop.domain.cart.repository.CartRepository;
//import kr.casealot.shop.domain.customer.entity.Customer;
//import kr.casealot.shop.domain.customer.repository.CustomerRepository;
//import kr.casealot.shop.domain.customer.service.CustomerService;
//import kr.casealot.shop.domain.product.entity.Product;
//import kr.casealot.shop.domain.product.repository.ProductRepository;
//import kr.casealot.shop.domain.product.service.ProductService;
//import kr.casealot.shop.global.common.APIResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CartService {
//    private final CustomerService customerService;
//    private final ProductService productService;
//    private final CustomerRepository customerRepository;
//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//    private final CartItemRepository cartItemRepository;
//
//    @Transactional
//    public APIResponse<CartItem> addCart(Customer customer, Product product, int amount) {
//
//        // 유저 id로 해당 유저의 장바구니 찾기
//        Cart cart = cartRepository.findByCustomerId(customer.getSeq());
//
//        // 장바구니가 존재하지 않는다면
//        if (cart == null) {
//            cart = Cart.createCart(customer);
//            cartRepository.save(cart);
//        }
//
//        Optional<Product> product1 = productRepository.findById(product.getId());
//        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), item.getId());
//
//        // 상품이 장바구니에 존재하지 않는다면 카트상품 생성 후 추가
//        if (cartItem == null) {
//            cartItem = CartItem.createCartItem(cart, product, amount);
//            cartItemRepository.save(cartItem);
//        }
//
//        // 상품이 장바구니에 이미 존재한다면 수량만 증가
//        else {
//            CartItem update = cartItem;
//            update.setCart(cartItem.getCart());
//            update.setProduct(cartItem.getProduct());
//            update.addCount(amount);
//            update.setCount(update.getCount());
//            cartItemRepository.save(update);
//        }
//
//        // 카트 상품 총 개수 증가
//        cart.setCount(cart.getCount()+amount);
//
//        return APIResponse.success("cartItem", cartItem);
//    }
//}
