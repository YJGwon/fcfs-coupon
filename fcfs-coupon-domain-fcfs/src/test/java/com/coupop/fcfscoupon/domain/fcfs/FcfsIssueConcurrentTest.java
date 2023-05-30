package com.coupop.fcfscoupon.domain.fcfs;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.coupop.fcfscoupon.client.coupon.CouponService;
import com.coupop.fcfscoupon.domain.fcfs.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.domain.fcfs.testconfig.DataSetup;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class FcfsIssueConcurrentTest {

    private static final LocalTime OPENED_TIME = FcfsIssuePolicy.getOpenAt();

    @Autowired
    private FcfsIssueService fcfsIssueService;

    @Autowired
    private DataSetup dataSetup;

    @MockBean
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        dataSetup.clean();
    }

    @DisplayName("100명의 사용자가 동시에 쿠폰 발급을 요청할 때, 정확한 수량만 발급한다.")
    @Test
    void issue_concurrent_100() throws InterruptedException {
        final int numberOfThreads = FcfsIssuePolicy.getLimit() * 3;
        final ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);
        final CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        final String emailFormat = "foo%d@bar.com";
        for (int i = 0; i < numberOfThreads; i++) {
            final int count = i;
            threadPool.submit(() -> {
                try {
                    fcfsIssueService.issue(String.format(emailFormat, count), OPENED_TIME);
                } catch (CouponOutOfStockException e) {
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        verify(couponService, times(FcfsIssuePolicy.getLimit())).issue(anyLong(), anyString());
    }
}
