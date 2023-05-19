package com.coupop.fcfscoupon.fcfsissue.model;

public interface FcfsIssueRepository {

    Long add(final String email);

    Long remove(final String email);

    Long getCount();
}
