package com.coupop.fcfscoupon.testconfig;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.coupop.fcfscoupon.coupon.model.RandomCodeGenerator;
import com.coupop.fcfscoupon.fcfsissue.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.fcfsissue.support.RequestTime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(MailSenderConfig.class)
public abstract class IntegrationTestConfig {

    protected static final String MOCKED_COUPON_VALUE = "fakevalue";

    @Autowired
    protected DatabaseSetUp databaseSetUp;

    @MockBean
    protected RequestTime requestTime;

    @MockBean
    protected RandomCodeGenerator codeGenerator;

    @BeforeEach
    void integrationSetup() {
        databaseSetUp.clean();

        given(requestTime.getValue())
                .willReturn(FcfsIssuePolicy.getOpenAt());

        given(codeGenerator.generate(anyLong()))
                .willReturn(MOCKED_COUPON_VALUE);
    }
}
