package com.coupop.fcfscoupon.domain.fcfs.testconfig;


import com.coupop.fcfscoupon.client.coupon.CouponWebService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class IntegrationTestConfig {

    protected static final String MOCKED_COUPON_VALUE = "fakevalue";

    @Autowired
    protected DatabaseSetUp databaseSetUp;

    @MockBean
    protected CouponWebService couponWebService;

    @BeforeEach
    void integrationSetup() {
        databaseSetUp.clean();
    }
}
