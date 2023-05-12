package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.execption.CouponOutOfStockException;
import com.coupop.fcfscoupon.model.Coupon;
import com.coupop.fcfscoupon.model.CouponCountRepository;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponCountRepository couponCountRepository;
    private final RequestTime requestTime;

    public CouponService(final CouponCountRepository couponCountRepository,
                         final RequestTime requestTime) {
        this.couponCountRepository = couponCountRepository;
        this.requestTime = requestTime;
    }

    public CouponResponse issue() {
        checkCouponOpen();
        checkCouponInStock();

        final Coupon coupon = new Coupon("뭔가 좋은 쿠폰");
        couponCountRepository.increaseCount();
        return CouponResponse.of(coupon);
    }

    private void checkCouponInStock() {
        if (CouponIssuePolicy.isCouponOutOfStock(couponCountRepository.getCount())) {
            throw new CouponOutOfStockException();
        }
    }

    private void checkCouponOpen() {
        if (CouponIssuePolicy.isCouponClosed(requestTime.getValue())) {
            throw new CouponNotOpenedException();
        }
    }
}
