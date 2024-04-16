package com.project.eat.order.coupon;

import com.project.eat.member.Coupon;
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
}
