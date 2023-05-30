package com.coupop.fcfscoupon.client.coupon;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

interface CouponWebService {

    @PostExchange("/issue")
    void issue(@RequestBody final IssuanceRequest issuanceRequest);

    @PostExchange("/resend")
    void resend(@RequestBody final ResendRequest resendRequest);
}
