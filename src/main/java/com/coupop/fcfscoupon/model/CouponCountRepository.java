package com.coupop.fcfscoupon.model;

public interface CouponCountRepository {

    int increaseCount();

    int getCount();

    void setCount(int count);
}
