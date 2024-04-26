package com.project.eat.order.coupon;

import com.project.eat.member.Coupon;
import com.project.eat.order.Order;
import com.project.eat.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;
    @Autowired
    private OrderService orderService;
    @Test
    void updateCoupon(){
        Order order = orderService.findOne(145L);

        couponService.couponRollback(order);

        Coupon findCoupon = couponService.findOne(28L);

        Assertions.assertThat(findCoupon.isCanUse()).isTrue();
    }

}