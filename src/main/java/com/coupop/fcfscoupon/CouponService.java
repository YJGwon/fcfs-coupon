package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponRequest;
import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.execption.CouponOutOfStockException;
import com.coupop.fcfscoupon.execption.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.model.Coupon;
import com.coupop.fcfscoupon.model.CouponIssuanceRepository;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponIssuanceRepository couponIssuanceRepository;

    private final RequestTime requestTime;

    public CouponService(final CouponIssuanceRepository couponIssuanceRepository,
                         final RequestTime requestTime) {
        this.couponIssuanceRepository = couponIssuanceRepository;
        this.requestTime = requestTime;
    }

    public CouponResponse issue(final CouponRequest request) {
        checkCouponOpen();
        final Long count = couponIssuanceRepository.getCount();
        checkCouponInStock(count.intValue());

        final String email = request.email();
        final Long result = couponIssuanceRepository.add(email);
        checkEmailNotUsedToday(email, result);

        final Coupon coupon = new Coupon("뭔가 좋은 쿠폰");
        return CouponResponse.of(coupon);
    }

    private void checkCouponOpen() {
        if (CouponIssuePolicy.isCouponClosed(requestTime.getValue())) {
            throw new CouponNotOpenedException();
        }
    }

    private void checkCouponInStock(final int count) {
        if (CouponIssuePolicy.isCouponOutOfStock(count)) {
            throw new CouponOutOfStockException();
        }
    }

    private void checkEmailNotUsedToday(final String email, final Long result) {
        if (result.equals(0L)) {
            throw new EmailAlreadyUsedException(email);
        }
    }
}
