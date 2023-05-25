package com.coupop.fcfscoupon.domain.coupon.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Disabled
@SpringBootTest
@Import(AsyncExecutor.class)
public class AsyncThreadPoolTest {

    @Autowired
    private AsyncExecutor executor;

    @DisplayName("스프링이 종료될 때 thread pool에 남은 작업이 정상적으로 완료된 후 종료된다.")
    @Test
    void gracefullyShutdown_ifSpringContextShutDown() throws InterruptedException {
        executor.someJob();
    }
}
