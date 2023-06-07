package kr.casealot.shop.domain.wishlist.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.wishlist.dto.WishlistReqDTO;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.domain.wishlist.repository.WishlistRepository;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final AuthTokenProvider authTokenProvider;
    private final CustomerRepository customerRepository;

    public void addWishlist(WishlistReqDTO wishlistReqDTO, HttpServletRequest request) {

        String customerId = findCustomerId(request);
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
//        if (wishlist == null) {
//            wishlist = createWishlist(request);
//        }

        Product product = productRepository.findById(wishlistReqDTO.getProductId()).orElseThrow();

//        WishlistItem wishlistItem = new WishlistItem();
//        wishlistItem.setProduct(product);
//        wishlistItem.setWishlist(wishlist);
//        wishlist.getWishlistItemList().add(wishlistItem);
//
//        wishlistRepository.save(wishlist);
//
//        WishlistResDTO wishlistResDTO = new WishlistResDTO();
//
//        List<Long> productIdList = wishlist.getWishlistItemList().stream()
//                .map(item -> item.getProduct().getId())
//                .collect(Collectors.toList());
//        wishlistResDTO.setPrductIdList(productIdList);
//
//        return APIResponse.success("wishlist", wishlistResDTO);
    }

    private Wishlist createWishlist(HttpServletRequest request) {
        Wishlist wishlist = new Wishlist();
        String customerId = findCustomerId(request);
        wishlist.setCustomer(customerRepository.findById(customerId));
        return wishlistRepository.save(wishlist);
    }

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }
}
