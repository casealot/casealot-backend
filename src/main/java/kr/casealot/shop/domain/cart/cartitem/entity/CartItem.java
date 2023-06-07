package kr.casealot.shop.domain.cart.cartitem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.casealot.shop.domain.cart.entity.Cart;
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
@Table(name = "CART_ITEM")
public class CartItem{
    @JsonIgnore
    @Id
    @Column(name = "CARTITEM_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CART_ID")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    private int quantity; // 상품 총 개수
}

