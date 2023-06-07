package kr.casealot.shop.domain.wishlist.entity;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wishlist")
public class Wishlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "CUSTOMER_SEQ")
    private Customer customer;

    @OneToMany(mappedBy = "wishlist")
    private List<WishlistItem> wishlistItemList;
}
