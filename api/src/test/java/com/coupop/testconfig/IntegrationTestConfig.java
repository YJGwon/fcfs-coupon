package com.coupop.testconfig;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.coupop.coupon.model.CouponEmailSender;
import com.coupop.coupon.model.RandomCodeGenerator;
import com.coupop.fcfsissue.model.FcfsIssuePolicy;
import com.coupop.fcfsissue.support.RequestTime;
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
    protected RequestTime requestTime;

    @MockBean
    protected RandomCodeGenerator codeGenerator;

    @MockBean
    protected CouponEmailSender couponEmailSender;

    @BeforeEach
    void integrationSetup() {
        databaseSetUp.cleanRedis();

        given(requestTime.getValue())
                .willReturn(FcfsIssuePolicy.getOpenAt());

        given(codeGenerator.generate(anyLong()))
                .willReturn(MOCKED_COUPON_VALUE);
    }
}
