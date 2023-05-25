package com.coupop.coupon;

import com.coupop.coupon.model.CouponIssueHistory;
import java.util.List;

@FunctionalInterface
public interface CouponIssueHistoriesResponseConverter<T> {

    T convert(final List<CouponIssueHistory> histories);
}
