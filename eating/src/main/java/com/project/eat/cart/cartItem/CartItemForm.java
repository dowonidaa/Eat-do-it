package com.project.eat.cart.cartItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemForm {

    private Long cartItemId;
    private Long itemId;
    private int price;
    private int quantity;
    private Long cartId;


}
