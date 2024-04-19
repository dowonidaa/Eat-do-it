package com.project.eat.member;

import com.project.eat.order.Order;
import com.project.eat.shop.ShopVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private MemberVO_JPA member;

    private String content;
    private int price;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shop_id")
    private ShopVO shop;

    private boolean canUse;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
