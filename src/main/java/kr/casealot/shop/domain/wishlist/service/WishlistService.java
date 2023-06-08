package kr.casealot.shop.domain.wishlist.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.wishlist.dto.WishlistReqDTO;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.domain.wishlist.repository.WishlistRepository;
import kr.casealot.shop.domain.wishlist.wishlistItem.dto.WishlistItemResDTO;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import kr.casealot.shop.domain.wishlist.wishlistItem.repository.WishlistItemRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
//    private final AuthTokenProvider authTokenProvider;
    private final CustomerRepository customerRepository;

    @Transactional
    public APIResponse<WishlistResDTO> addProductToWishlist(WishlistReqDTO wishlistReqDTO, HttpServletRequest request, Principal principal) {

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

    @Transactional
    public APIResponse<WishlistResDTO> deleteProductToWishlist(WishlistReqDTO wishlistReqDTO, HttpServletRequest request, Principal principal) {

        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Product product = productRepository.findById(wishlistReqDTO.getProductId()).orElseThrow();

        List<WishlistItem> wishlistItems = wishlist.getWishlistItemList();
        wishlistItems.removeIf(item -> item.getProduct().getId().equals(product.getId()));


        WishlistItem wishlistItemToDelete = wishlistItemRepository.findByProductAndWishlist(product, wishlist);
        if (wishlistItemToDelete != null) {
            wishlistItemRepository.delete(wishlistItemToDelete);

            WishlistResDTO wishlistResDTO = WishlistResDTO.builder()
                    .customerId(customerId)
                    .productId(product.getId())
                    .productName(product.getName())
                    .build();

            return APIResponse.success("wishlist", wishlistResDTO);
        } else {
            return APIResponse.notExistRequest();
        }
    }

    public APIResponse<List<WishlistItemResDTO>> getWishlistItems(Principal principal) {

        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        List<WishlistItem> wishlistItems = wishlist.getWishlistItemList();

        List<WishlistItemResDTO> wishlistItemResDTOList = new ArrayList<>();

        for (WishlistItem wishlistItem : wishlistItems) {
            Product product = wishlistItem.getProduct();
            WishlistItemResDTO wishlistItemResDTO = WishlistItemResDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .thumbnail(product.getThumbnail().getUrl())
                    .content(product.getContent())
                    .color(product.getColor())
                    .season(product.getSeason())
                    .type(product.getType())
                    .build();

            wishlistItemResDTOList.add(wishlistItemResDTO);
        }
        return APIResponse.success("wishlist", wishlistItemResDTOList);
    }

    @Transactional
    public Wishlist createWishlist(Customer customer){
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);

        return wishlist;
    }
}
