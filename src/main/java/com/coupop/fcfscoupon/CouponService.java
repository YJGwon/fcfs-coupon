package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponRequest;
import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.execption.CouponOutOfStockException;
import com.coupop.fcfscoupon.execption.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.model.Coupon;
import com.coupop.fcfscoupon.model.CouponCountRepository;
import com.coupop.fcfscoupon.model.CouponEmailRepository;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponCountRepository couponCountRepository;
    private final CouponEmailRepository couponEmailRepository;

    private final RequestTime requestTime;

    public CouponService(final CouponCountRepository couponCountRepository,
                         final CouponEmailRepository couponEmailRepository,
                         final RequestTime requestTime) {
        this.couponCountRepository = couponCountRepository;
        this.couponEmailRepository = couponEmailRepository;
        this.requestTime = requestTime;
    }

    public CouponResponse issue(final CouponRequest request) {
        checkCouponOpen();
        final int count = couponCountRepository.getCount();
        checkCouponInStock(count);

        final Long increasedCount = couponCountRepository.increaseCount();
        checkOverIssue(increasedCount);

        final String email = request.email();
        final Long result = couponEmailRepository.addEmail(email);
        checkEmailNotUsedToday(email, result); // decrease count

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

    private void checkOverIssue(final Long issuedCount) {
        if (CouponIssuePolicy.isCouponOverIssued(issuedCount)) {
            couponCountRepository.decreaseCount();
            throw new CouponOutOfStockException();
        }
    }

    private void checkEmailNotUsedToday(final String email, final Long result) {
        if (result.equals(0L)) {
            couponCountRepository.decreaseCount();
            throw new EmailAlreadyUsedException(email);
        }
    }
}
