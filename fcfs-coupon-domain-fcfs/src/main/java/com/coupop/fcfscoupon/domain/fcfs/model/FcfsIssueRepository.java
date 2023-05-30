package com.coupop.fcfscoupon.domain.fcfs.model;

public interface FcfsIssueRepository {

    Long add(final String email);

    Long getCount();
}
