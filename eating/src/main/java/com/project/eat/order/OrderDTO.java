package com.project.eat.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private int price;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private String PaymentMethod;
    private Long shopId;
    private String shopThum;
    private LocalDateTime orderDate;
    private String shopName;
    private String itemsName;
    private boolean reviewExists;

}

