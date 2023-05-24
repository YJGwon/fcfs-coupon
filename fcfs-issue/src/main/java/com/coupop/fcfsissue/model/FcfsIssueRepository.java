package com.coupop.fcfsissue.model;

public interface FcfsIssueRepository {

    Long add(final String email);

    Long remove(final String email);

    Long getCount();
}
