package com.coupop.fcfscoupon.coupon.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Coupon {

    private final String value;

    public Coupon(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
