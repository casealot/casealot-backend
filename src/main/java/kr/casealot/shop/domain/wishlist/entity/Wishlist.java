package kr.casealot.shop.domain.wishlist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
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
@Table(name = "WISHLIST")
public class Wishlist{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_SEQ")
    private Customer customer;

    @Builder.Default
    @JsonBackReference
    @OneToMany(mappedBy = "wishlist", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WishlistItem> wishlistItemList = new ArrayList<>();
}
