package com.coupop.fcfscoupon.client.coupon;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import com.coupop.fcfscoupon.api.coupon.dto.SendRequest;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponWebService couponWebService;

    public CouponService(final CouponWebService couponWebService) {
        this.couponWebService = couponWebService;
    }

    public void issue(final Long seq, final String email) {
        couponWebService.issue(new IssuanceRequest(seq, email));
    }

    public void send(final String couponId, final String email) {
        couponWebService.send(new SendRequest(couponId, email));
    }

    public void resend(final String historyId) {
        couponWebService.resend(new ResendRequest(historyId));
    }
}
