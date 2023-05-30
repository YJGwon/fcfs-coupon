package com.coupop.fcfscoupon.client.coupon;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import com.coupop.fcfscoupon.api.coupon.dto.SendRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface CouponWebService {

    @PostExchange("/issue")
    void issue(@RequestBody final IssuanceRequest issuanceRequest);

    @PostExchange("/send")
    void send(@RequestBody final SendRequest sendRequest);

    @PostExchange("/resend")
    void resend(@RequestBody final ResendRequest resendRequest);

    default void issue(final Long seq, final String email) {
        issue(new IssuanceRequest(seq, email));
    }

    default void send(final String couponId, final String email) {
        send(new SendRequest(couponId, email));
    }

    default void resend(final String historyId) {
        resend(new ResendRequest(historyId));
    }
}
