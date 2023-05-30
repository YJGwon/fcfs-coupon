package com.coupop.fcfscoupon.domain.coupon.model;

import java.util.Optional;

public interface CouponRepository {

    Coupon insert(final Coupon coupon);

    Optional<Coupon> findById(final String id);
}
