package com.coupop.fcfscoupon.api.coupon.testconfig;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.coupop.fcfscoupon.CouponApplication;
import com.coupop.fcfscoupon.domain.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.domain.coupon.model.RandomCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CouponApplication.class)
public abstract class IntegrationTestConfig {

    protected static final String MOCKED_COUPON_VALUE = "fakevalue";

    @Autowired
    protected DataSetup dataSetup;

    @MockBean
    protected RandomCodeGenerator codeGenerator;

    @MockBean
    protected CouponEmailSender couponEmailSender;

    @BeforeEach
    void integrationSetup() {
        dataSetup.clean();

        given(codeGenerator.generate(anyLong()))
                .willReturn(MOCKED_COUPON_VALUE);
    }
}
