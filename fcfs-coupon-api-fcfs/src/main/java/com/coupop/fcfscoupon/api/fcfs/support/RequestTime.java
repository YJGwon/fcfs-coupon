package com.coupop.fcfscoupon.api.fcfs.support;

import java.time.LocalTime;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@Value
@NonFinal public class RequestTime {

    LocalTime value;

    public RequestTime() {
        this.value = LocalTime.now();
    }
}
