package com.coupop.fcfscoupon.fcfsissue.exception;


import com.coupop.core.exception.BadRequestException;
import com.coupop.fcfscoupon.fcfsissue.model.FcfsIssuePolicy;

public class CouponNotOpenedException extends BadRequestException {

    private static final String TITLE = "쿠폰이 아직 오픈되지 않았습니다.";
    private static final String DETAIL = String.format("쿠폰은 %d시 %d분에 오픈됩니다.",
            FcfsIssuePolicy.getOpenAt().getHour(), FcfsIssuePolicy.getOpenAt().getMinute());

    public CouponNotOpenedException() {
        super(TITLE, DETAIL);
    }
}
