package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.model.Coupon;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final RequestTime requestTime;

    public CouponService(final RequestTime requestTime) {
        this.requestTime = requestTime;
    }

    public CouponResponse issue() {
        if (CouponIssuePolicy.isCouponClosed(requestTime.getValue())) {
            throw new CouponNotOpenedException();
        }

        final Coupon coupon = new Coupon("뭔가 좋은 쿠폰");
        return CouponResponse.of(coupon);
    }
}
