package com.coupop.fcfscoupon.model;

public interface CouponCountRepository {

    Long increaseCount();

    Long decreaseCount();

    int getCount();

    void setCount(int count);
}
