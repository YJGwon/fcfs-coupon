package com.coupop.fcfscoupon.fcfsissue.exception;

import com.coupop.fcfscoupon.exception.ApiException;
import com.coupop.fcfscoupon.fcfsissue.model.FcfsIssuePolicy;
import org.springframework.http.HttpStatus;

public class CouponOutOfStockException extends ApiException {

    private static final String TITLE = "쿠폰이 모두 소진되었습니다.";
    private static final String DETAIL = String.format(
            "오늘의 쿠폰 수량 %d장이 모두 소진되었습니다. 내일 %d시 %d분에 다시 이용해주세요.",
            FcfsIssuePolicy.getLimit(),
            FcfsIssuePolicy.getOpenAt().getHour(), FcfsIssuePolicy.getOpenAt().getMinute());

    public CouponOutOfStockException() {
        super(TITLE, DETAIL, HttpStatus.BAD_REQUEST);
    }
}