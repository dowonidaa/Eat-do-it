package com.project.eat.order;

public enum OrderStatus {
    READY("결제 준비"),
    PAYMENT("결제완료"),
    CHECKING("주문확인중"),
    COOKING("주문요리중"),
    DELIVERY("배달중"),
    COMPLETE("배달완료"),
    CANCEL("주문취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
