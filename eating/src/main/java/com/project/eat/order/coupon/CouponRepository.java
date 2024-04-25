package com.project.eat.order.coupon;

import com.project.eat.member.Coupon;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepository {

    private final EntityManager em;

    public Coupon findOne(Long couponId) {
        return em.find(Coupon.class, couponId);
    }

    public void deleteCoupon(Coupon coupon) {
        em.remove(coupon);
    }
}

