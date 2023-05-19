package com.coupop.fcfscoupon.model;

public interface FcfsIssueRepository {

    Long add(final String email);

    Long remove(final String email);

    Long getCount();
}
