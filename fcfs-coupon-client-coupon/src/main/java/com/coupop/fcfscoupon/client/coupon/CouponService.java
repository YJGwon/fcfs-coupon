package com.coupop.fcfscoupon.client.coupon;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponWebService couponWebService;

    public void issue(final Long seq, final String email) {
        couponWebService.issue(new IssuanceRequest(seq, email));
    }

    public void resend(final String historyId) {
        couponWebService.resend(new ResendRequest(historyId));
    }
}
