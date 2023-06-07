package kr.casealot.shop.domain.cart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CART")
public class Cart {
    @JsonIgnore
    @Id
    @Column(name = "CART_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CUSTOMER_ID")
    private Customer customer;

    @JsonBackReference
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    private int quantity; //장바구니에 담긴 총 상품 수

    public static Cart createCart(Customer customer) {
        Cart cart = new Cart();
        cart.setQuantity(0);
        cart.setCustomer(customer);
        return cart;
    }
}

