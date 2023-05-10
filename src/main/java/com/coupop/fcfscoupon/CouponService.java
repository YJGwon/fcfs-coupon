package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.model.Coupon;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    public CouponResponse issue() {
        // TODO: 쿠폰 발급 허용 시간 검증
        final Coupon coupon = new Coupon("뭔가 좋은 쿠폰");
        return CouponResponse.of(coupon);
    }
}
