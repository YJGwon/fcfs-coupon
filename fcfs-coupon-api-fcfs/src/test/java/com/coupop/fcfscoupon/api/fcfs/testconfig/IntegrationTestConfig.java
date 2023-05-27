package com.coupop.fcfscoupon.api.fcfs.testconfig;


import static org.mockito.BDDMockito.given;

import com.coupop.fcfscoupon.FcfsCouponApplication;
import com.coupop.fcfscoupon.api.fcfs.support.RequestTime;
import com.coupop.fcfscoupon.client.coupon.CouponWebService;
import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssuePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = FcfsCouponApplication.class)
public abstract class IntegrationTestConfig {

    protected static final String MOCKED_COUPON_VALUE = "fakevalue";

    @Autowired
    protected DatabaseSetUp databaseSetUp;

    @MockBean
    protected RequestTime requestTime;

    @MockBean
    protected CouponWebService couponWebService;

    @BeforeEach
    void integrationSetup() {
        databaseSetUp.cleanRedis();

        given(requestTime.getValue())
                .willReturn(FcfsIssuePolicy.getOpenAt());
    }
}
