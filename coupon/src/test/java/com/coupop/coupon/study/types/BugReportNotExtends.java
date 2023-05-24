package com.coupop.coupon.study.types;

public class BugReportNotExtends {

    private final String title;
    private final String content;
    private final String environment;

    public BugReportNotExtends(final String title, final String content, final String environment) {
        this.title = title;
        this.content = content;
        this.environment = environment;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getEnvironment() {
        return environment;
    }
}
