package kr.casealot.shop.domain.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CART")
public class Cart extends BaseTimeEntity {
    @JsonIgnore
    @Id
    @Column(name = "CART_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CUSTOMER_ID")
    private Customer customer;

    @OneToMany
    private List<CartItem> cartItems;

    private int quantity; //장바구니에 담긴 총 상품 수

    public static Cart createCart(Customer customer) {
        Cart cart = new Cart();
        cart.setQuantity(0);
        cart.setCustomer(customer);
        return cart;
    }

    public CartItem getCartItemByProduct(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().equals(product)) {
                return item;
            }
        }
        return null;
    }
}

