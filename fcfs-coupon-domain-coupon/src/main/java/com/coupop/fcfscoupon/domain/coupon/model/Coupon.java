package com.coupop.fcfscoupon.domain.coupon.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Coupon {

    private final String value;

    @Id
    private String id;
}

