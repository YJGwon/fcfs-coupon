package com.coupop.fcfscoupon.execption;


import com.coupop.fcfscoupon.model.FcfsIssuePolicy;
import org.springframework.http.HttpStatus;

public class CouponNotOpenedException extends ApiException {

    private static final String TITLE = "쿠폰이 아직 오픈되지 않았습니다.";
    private static final String DETAIL = String.format("쿠폰은 %d시 %d분에 오픈됩니다.",
            FcfsIssuePolicy.getOpenAt().getHour(), FcfsIssuePolicy.getOpenAt().getMinute());

    public CouponNotOpenedException() {
        super(TITLE, DETAIL, HttpStatus.BAD_REQUEST);
    }
}
