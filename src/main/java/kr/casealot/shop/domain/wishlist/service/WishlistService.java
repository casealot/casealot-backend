package kr.casealot.shop.domain.wishlist.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.domain.wishlist.repository.WishlistRepository;
import kr.casealot.shop.domain.wishlist.wishlistItem.dto.WishlistItemDTO;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
import kr.casealot.shop.domain.wishlist.wishlistItem.repository.WishlistItemRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.AlreadyDeletedException;
import kr.casealot.shop.global.exception.DuplicateProductException;
import kr.casealot.shop.global.exception.NotFoundProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final String API_NAME = "wishlist";

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public APIResponse<WishlistResDTO> addProductToWishlist(Long productId,
                                                            Principal principal) throws DuplicateProductException {
        String customerId = principal.getName();

        Customer customer = customerRepository.findById(customerId);
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);

        if (wishlist == null) {
            wishlist = createWishlist(customer);
            wishlistRepository.save(wishlist);
        }

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            throw new NotFoundProductException();
        }

        boolean isExist = wishlist.getWishlistItemList().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));

        if (isExist) {
            throw new DuplicateProductException();
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);
        wishlist.getWishlistItemList().add(wishlistItem);

        wishlistItemRepository.save(wishlistItem);

        WishlistResDTO wishlistResDTO = createWishlistResDTO(wishlist);
        return APIResponse.success(API_NAME, wishlistResDTO);
    }

    @Transactional
    public APIResponse<WishlistResDTO> getWishlistItems(Principal principal) {

        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Customer customer = customerRepository.findCustomerById(customerId);

        if (wishlist == null) {
            wishlist = createWishlist(customer);
            wishlistRepository.save(wishlist);
        }

        List<WishlistItem> wishlistItems = wishlist.getWishlistItemList();

        List<WishlistItemDTO> wishlistItemDTOList = wishlistItems.stream()
                .map(wishlistItem -> createWishlistItemDTO(wishlistItem.getProduct()))
                .collect(Collectors.toList());

        WishlistResDTO wishlistResDTO = WishlistResDTO.builder()
                .customerId(customerId)
                .wishlistId(wishlist.getId())
                .productList(wishlistItemDTOList)
                .build();

        return APIResponse.success(API_NAME, wishlistResDTO);
    }

    @Transactional
    public APIResponse<WishlistResDTO> deleteProductToWishlist(Long productId, Principal principal) {
        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        Product product = productRepository.findById(productId).orElseThrow(NotFoundProductException::new);

        List<WishlistItem> wishlistItems = wishlist.getWishlistItemList();
        boolean isItemDeleted = wishlistItems.removeIf(item -> item.getProduct().getId().equals(product.getId()));

        if (isItemDeleted) {
            wishlistItemRepository.deleteByProductAndWishlist(product, wishlist);

            WishlistResDTO wishlistResDTO = createWishlistResDTO(wishlist);
            return APIResponse.success(API_NAME, wishlistResDTO);
        } else {
            throw new AlreadyDeletedException();
        }
    }

    @Transactional
    public APIResponse<WishlistResDTO> deleteWishlist(Principal principal) {
        String customerId = principal.getName();
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);

        wishlistRepository.deleteCartItemsByCart(wishlist);
        wishlistRepository.delete(wishlist);

        WishlistResDTO wishlistResDTO = new WishlistResDTO();
        wishlistResDTO.setCustomerId(customerId);
        wishlistResDTO.setWishlistId(wishlist.getId());
        wishlistResDTO.setProductList(Collections.emptyList());

        return APIResponse.success(API_NAME, wishlistResDTO);
    }

    public int getCustomerCountForProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new NotFoundProductException();
        }

        List<WishlistItem> wishlistItems = wishlistItemRepository.findByProduct(product);

        Set<String> uniqueCustomers = new HashSet<>();
        for (WishlistItem wishlistItem : wishlistItems) {
            uniqueCustomers.add(wishlistItem.getCustomerId());
        }

        return uniqueCustomers.size();
    }

    @Transactional
    public Wishlist createWishlist(Customer customer) {
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setWishlistItemList(new ArrayList<>());
        return wishlist;
    }

    private WishlistItemDTO createWishlistItemDTO(Product product) {
        WishlistItemDTO wishlistItemDTO = new WishlistItemDTO();
        wishlistItemDTO.setId(product.getId());
        wishlistItemDTO.setName(product.getName());
        wishlistItemDTO.setPrice(product.getPrice());
        if (product.getThumbnail() != null) {
            wishlistItemDTO.setThumbnail(product.getThumbnail().getUrl());
        } else {
            wishlistItemDTO.setThumbnail(null);
        }
        wishlistItemDTO.setContent(product.getContent());
        wishlistItemDTO.setColor(product.getColor());
        wishlistItemDTO.setSeason(product.getSeason());
        wishlistItemDTO.setType(product.getType());
        return wishlistItemDTO;
    }

    private WishlistResDTO createWishlistResDTO(Wishlist wishlist) {
        WishlistResDTO wishlistResDTO = new WishlistResDTO();
        wishlistResDTO.setCustomerId(wishlist.getCustomer().getId());
        wishlistResDTO.setWishlistId(wishlist.getId());
        List<WishlistItemDTO> productList = wishlist.getWishlistItemList().stream()
                .map(item -> createWishlistItemDTO(item.getProduct()))
                .collect(Collectors.toList());
        wishlistResDTO.setProductList(productList);
        return wishlistResDTO;
    }
}
