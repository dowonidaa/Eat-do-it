package com.project.eat.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartForm {

    private Long cartId;
    private Long shopId;
    private String memberId;
    private String orderType;
    private int totalPrice;

}
