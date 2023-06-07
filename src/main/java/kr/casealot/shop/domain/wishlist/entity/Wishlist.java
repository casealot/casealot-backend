package kr.casealot.shop.domain.wishlist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wishlist")
public class Wishlist{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_SEQ")
    private Customer customer;

    @Builder.Default
    @JsonBackReference
    @OneToMany(mappedBy = "wishlist", fetch = FetchType.EAGER)
    private List<WishlistItem> wishlistItemList = new ArrayList<>();
}
