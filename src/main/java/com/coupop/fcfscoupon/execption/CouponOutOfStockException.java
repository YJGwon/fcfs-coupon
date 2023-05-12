package com.coupop.fcfscoupon.execption;

import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import org.springframework.http.HttpStatus;

public class CouponOutOfStockException extends ApiException {

    private static final String TITLE = "쿠폰이 모두 소진되었습니다.";
    private static final String DETAIL = String.format(
            "오늘의 쿠폰 수량 %d장이 모두 소진되었습니다. 내일 %d시 %d분에 다시 이용해주세요.",
            CouponIssuePolicy.getLimit(),
            CouponIssuePolicy.getOpenAt().getHour(), CouponIssuePolicy.getOpenAt().getMinute());

    public CouponOutOfStockException() {
        super(TITLE, DETAIL, HttpStatus.BAD_REQUEST);
    }
}
