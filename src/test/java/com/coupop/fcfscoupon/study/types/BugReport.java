package com.coupop.fcfscoupon.study.types;

public final class BugReport extends Report {

    private final String environment;

    public BugReport(final String title, final String content, final String environment) {
        super(title, content);
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }
}
