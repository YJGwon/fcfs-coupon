package com.coupop.fcfscoupon.model;

public class Coupon {

    private final String value;

    private Coupon(final String value) {
        this.value = value;
    }

    public Coupon(final Long sequence, final String secretKey) {
        this(RandomCodeGenerator.generate(sequence, secretKey));
    }

    public String getValue() {
        return value;
    }
}
