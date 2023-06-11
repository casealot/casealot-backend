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
@Table(name = "WISHLIST_ITEM")
public class WishlistItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WISHLIST_ID")
    private Wishlist wishlist;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public String getCustomerId() {
        if (wishlist != null) {
            return wishlist.getCustomer().getId();
        }
        return null;
    }
}
