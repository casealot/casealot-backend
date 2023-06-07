package kr.casealot.shop.domain.wishlist.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.wishlist.dto.WishlistReqDTO;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.domain.wishlist.repository.WishlistRepository;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import kr.casealot.shop.domain.wishlist.wishlistItem.repository.WishlistItemRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
//    private final AuthTokenProvider authTokenProvider;
    private final CustomerRepository customerRepository;

    public APIResponse<WishlistResDTO> addWishlist(WishlistReqDTO wishlistReqDTO, HttpServletRequest request, Principal principal) {

        String customerId = principal.getName();

        Customer customer = customerRepository.findById(customerId);
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Product product = productRepository.findById(wishlistReqDTO.getProductId()).orElseThrow();

        if (wishlist == null) {
            wishlist = createWishlist(customer);
            wishlist.setWishlistItemList(new ArrayList<>());
            wishlistRepository.save(wishlist);
        }

        boolean isExist = wishlist.getWishlistItemList().stream()
                .anyMatch(item -> item.getProduct().getId().equals(wishlistReqDTO.getProductId()));

        if(isExist){
            return APIResponse.alreadyExistRequest();
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);
        wishlist.getWishlistItemList().add(wishlistItem);

        wishlistItemRepository.save(wishlistItem);

        WishlistResDTO wishlistResDTO = WishlistResDTO.builder()
                                        .customerId(customerId)
                                        .productId(product.getId())
                                        .productName(product.getName())
                                        .build();

        wishlistRepository.save(wishlist);

        return APIResponse.success("wishlist", wishlistResDTO);
    }

    public static Wishlist createWishlist(Customer customer){
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);

        return wishlist;
    }
}
