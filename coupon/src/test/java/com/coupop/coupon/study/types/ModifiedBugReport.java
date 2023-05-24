package com.coupop.coupon.study.types;

public final class ModifiedBugReport extends Report {

    private final String environment;
    private final String priority;

    public ModifiedBugReport(final String title, final String content, final String environment,
                             final String priority) {
        super(title, content);
        this.environment = environment;
        this.priority = priority;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getPriority() {
        return priority;
    }
}
