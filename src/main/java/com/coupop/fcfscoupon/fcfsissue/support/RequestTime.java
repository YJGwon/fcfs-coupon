package com.coupop.fcfscoupon.fcfsissue.support;

import java.time.LocalTime;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RequestTime {

    private final LocalTime value;

    public RequestTime() {
        this.value = LocalTime.now();
    }

    public LocalTime getValue() {
        return value;
    }
}
