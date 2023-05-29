package com.coupop.fcfscoupon.domain.coupon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncExecutor {

    private Logger log = LoggerFactory.getLogger(AsyncExecutor.class);

    @Async
    public void someJob() throws InterruptedException {
        log.info("execution start");
        Thread.sleep(5000);
        log.info("execution end");
    }
}
