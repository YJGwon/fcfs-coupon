package com.coupop.fcfscoupon.study.types;

public class Report {

    private final String title;
    private final String content;

    protected Report(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
