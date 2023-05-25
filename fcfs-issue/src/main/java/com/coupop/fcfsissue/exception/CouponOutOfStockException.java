package com.coupop.fcfsissue.exception;

import com.coupop.fcfscoupon.common.exception.BadRequestException;
import com.coupop.fcfsissue.model.FcfsIssuePolicy;

public class CouponOutOfStockException extends BadRequestException {

    private static final String TITLE = "쿠폰이 모두 소진되었습니다.";
    private static final String DETAIL = String.format(
            "오늘의 쿠폰 수량 %d장이 모두 소진되었습니다. 내일 %d시 %d분에 다시 이용해주세요.",
            FcfsIssuePolicy.getLimit(),
            FcfsIssuePolicy.getOpenAt().getHour(), FcfsIssuePolicy.getOpenAt().getMinute());

    public CouponOutOfStockException() {
        super(TITLE, DETAIL);
    }
}
