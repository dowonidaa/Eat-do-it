package com.project.eat.order.coupon;

import com.project.eat.member.Coupon;
import com.project.eat.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon findOne(Long couponId) {
        return couponRepository.findOne(couponId);
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon one = couponRepository.findOne(couponId);
        couponRepository.deleteCoupon(one);

    }

    @Transactional
    public void couponUsed(Long couponId, Order order) {
        Coupon findCoupon = couponRepository.findOne(couponId);
        findCoupon.setCanUse(false);
        findCoupon.setOrder(order);
    }

    @Transactional
    public void couponRollback(Order order) {
        Coupon coupon = couponRepository.findOne(order.getCoupon().getId());
        coupon.setCanUse(true);
        coupon.setOrder(null);

    }
}
