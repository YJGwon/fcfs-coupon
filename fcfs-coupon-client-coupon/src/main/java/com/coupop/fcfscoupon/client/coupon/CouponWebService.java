package com.coupop.fcfscoupon.client.coupon;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface CouponWebService {

    @PostExchange("/issue")
    void issue(@RequestBody final IssuanceRequest issuanceRequest);

    default void issue(final Long seq, final String email) {
        issue(new IssuanceRequest(seq, email));
    }

}
