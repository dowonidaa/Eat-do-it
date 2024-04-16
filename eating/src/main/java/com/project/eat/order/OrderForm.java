package com.project.eat.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {

    private Long id;
    private int orderPrice;
    private int totalPrice;
    private String orderAddress;
    private String orderAddressDetail;
    private String orderTel;
    private int discount;
    private OrderType orderType;
    private String orderStatus;
    private String PaymentMethod;
    private String memberNotes;



}
