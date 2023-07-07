package kr.casealot.shop.domain.cart.dto;

import java.util.stream.Collectors;
import kr.casealot.shop.domain.cart.cartitem.entity.CartItem;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.product.dto.ProductCartDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartGetDTO {

    private Long customerSeq;
    private String customerName;
    private Long cartId;
    private List<ProductCartDTO> products; // 각 상품의 정보를 담을 리스트

    public static ProductCartDTO buildProductCartDTO(CartItem cartItem) {
        ProductCartDTO productCartDTO = new ProductCartDTO();
        productCartDTO.setId(cartItem.getProduct().getId());
        productCartDTO.setName(cartItem.getProduct().getName());
        productCartDTO.setPrice(cartItem.getProduct().getPrice());
        productCartDTO.setCalculatePrice(cartItem.getProduct().getCalculatePrice());
        productCartDTO.setQuantity(cartItem.getQuantity());
        if (cartItem.getProduct().getThumbnail() == null) {
            productCartDTO.setThumbnail(null);
        } else {
            productCartDTO.setThumbnail(cartItem.getProduct().getThumbnail().getUrl());
        }
        productCartDTO.setContent(cartItem.getProduct().getContent());
        productCartDTO.setColor(cartItem.getProduct().getColor());
        productCartDTO.setSeason(cartItem.getProduct().getSeason());
        productCartDTO.setType(cartItem.getProduct().getType());
        return productCartDTO;
    }

    public static CartGetDTO buildCartGetDTO(Customer customer, Cart cart, List<CartItem> cartItems) {
        CartGetDTO cartGetDto = new CartGetDTO();
        cartGetDto.setCustomerSeq(customer.getSeq());
        cartGetDto.setCustomerName(customer.getName());
        cartGetDto.setCartId(cart.getSeq());
        cartGetDto.setProducts(cartItems.stream()
            .map(CartGetDTO::buildProductCartDTO)
            .collect(Collectors.toList()));
        return cartGetDto;
    }


}
