package com.coupop.coupon.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Coupon {

    private final String value;

    @Id
    private String id;

    public Coupon(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
