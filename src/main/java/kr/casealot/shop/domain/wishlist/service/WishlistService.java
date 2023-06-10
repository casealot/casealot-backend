package kr.casealot.shop.domain.wishlist.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
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
    private final String API_NAME = "wishlist";

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public APIResponse<WishlistItemResDTO> addProductToWishlist(Long productId, HttpServletRequest request, Principal principal) {

        String customerId = principal.getName();

        Customer customer = customerRepository.findById(customerId);
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Product product = productRepository.findById(productId).orElseThrow();

        if (wishlist == null) {
            wishlist = createWishlist(customer);
            wishlist.setWishlistItemList(new ArrayList<>());
            wishlistRepository.save(wishlist);
        }

        boolean isExist = wishlist.getWishlistItemList().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));

        if (isExist) {
            return APIResponse.alreadyExistRequest();
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);
        wishlist.getWishlistItemList().add(wishlistItem);

        wishlistItemRepository.save(wishlistItem);

        WishlistItemResDTO wishlistItemResDTO = WishlistItemResDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .content(product.getContent())
                .color(product.getColor())
                .season(product.getSeason())
                .type(product.getType())
                .build();

        if (product.getThumbnail() != null) {
            wishlistItemResDTO.setThumbnail(product.getThumbnail().getUrl());
        } else {
            wishlistItemResDTO.setThumbnail(null);
        }

        return APIResponse.success(API_NAME, wishlistItemResDTO);
    }

    @Transactional
    public APIResponse<WishlistItemResDTO> deleteProductToWishlist(Long productId, HttpServletRequest request, Principal principal) {

        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Product product = productRepository.findById(productId).orElseThrow();

        List<WishlistItem> wishlistItems = wishlist.getWishlistItemList();
        boolean isItemDeleted = wishlistItems.removeIf(item -> item.getProduct().getId().equals(product.getId()));

        if (isItemDeleted) {
            WishlistItemResDTO deletedItemResDTO = WishlistItemResDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .content(product.getContent())
                    .color(product.getColor())
                    .season(product.getSeason())
                    .type(product.getType())
                    .build();

            if (product.getThumbnail() != null) {
                deletedItemResDTO.setThumbnail(product.getThumbnail().getUrl());
            } else {
                deletedItemResDTO.setThumbnail(null);
            }

            wishlistItemRepository.deleteByProductAndWishlist(product, wishlist);

            return APIResponse.success(API_NAME, deletedItemResDTO);
        } else {
            return APIResponse.notExistRequest();
        }
    }


    public APIResponse<List<WishlistItemResDTO>> getWishlistItems(Principal principal) {

        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Customer customer = customerRepository.findCustomerById(customerId);

        if (wishlist == null) {
            wishlist = createWishlist(customer);
            wishlist.setWishlistItemList(new ArrayList<>());
            wishlistRepository.save(wishlist);
        }

        List<WishlistItem> wishlistItems = wishlist.getWishlistItemList();

        List<WishlistItemResDTO> wishlistItemResDTOList = new ArrayList<>();

        for (WishlistItem wishlistItem : wishlistItems) {
            Product product = wishlistItem.getProduct();
            WishlistItemResDTO wishlistItemResDTO = WishlistItemResDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .content(product.getContent())
                    .color(product.getColor())
                    .season(product.getSeason())
                    .type(product.getType())
                    .build();

            if (product.getThumbnail() != null) {
                wishlistItemResDTO.setThumbnail(product.getThumbnail().getUrl());
            } else {
                wishlistItemResDTO.setThumbnail(null);
            }

            wishlistItemResDTOList.add(wishlistItemResDTO);
        }
        return APIResponse.success(API_NAME, wishlistItemResDTOList);
    }

    @Transactional
    public Wishlist createWishlist(Customer customer){
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);

        return wishlist;
    }

    @Transactional
    public APIResponse<List<WishlistItemResDTO>> deleteWishlist(Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());
        Wishlist wishlist = wishlistRepository.findByCustomerId(principal.getName());

        List<WishlistItem> deletedItems = wishlist.getWishlistItemList();

        wishlistRepository.deleteCartItemsByCart(wishlist);
        wishlistRepository.delete(wishlist);

        List<WishlistItemResDTO> deletedItemResDTOList = new ArrayList<>();

        for (WishlistItem deletedItem : deletedItems) {
            Product product = deletedItem.getProduct();
            WishlistItemResDTO deletedItemResDTO = WishlistItemResDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .content(product.getContent())
                    .color(product.getColor())
                    .season(product.getSeason())
                    .type(product.getType())
                    .build();

            if (product.getThumbnail() != null) {
                deletedItemResDTO.setThumbnail(product.getThumbnail().getUrl());
            } else {
                deletedItemResDTO.setThumbnail(null);
            }

            deletedItemResDTOList.add(deletedItemResDTO);
        }

        return APIResponse.success( API_NAME, deletedItemResDTOList);
    }

}
