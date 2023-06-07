package kr.casealot.shop.domain.wishlist.wishlistItem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wishlist_item")
public class WishlistItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
}
