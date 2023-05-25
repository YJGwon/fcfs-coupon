package com.coupop.fcfscoupon.domain.coupon;

import com.coupop.fcfscoupon.domain.coupon.model.CouponIssueHistory;
import java.util.List;

@FunctionalInterface
public interface CouponIssueHistoriesResponseConverter<T> {

    T convert(final List<CouponIssueHistory> histories);
}
