package com.coupop.fcfscoupon.model;

public interface CouponCountRepository {

    void increaseCount();

    int getCount();

    void setCount(int count);
}
