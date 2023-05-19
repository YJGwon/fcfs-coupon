package com.coupop.fcfscoupon.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.RandomCodeGenerator;
import com.coupop.fcfscoupon.testconfig.DatabaseSetUp;
import com.coupop.fcfscoupon.testconfig.MailSenderConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
@Import(MailSenderConfig.class)
class CouponServiceTest {

    private static final String MOCKED_COUPON_VALUE = "fakevalue";

    @Autowired
    private CouponService couponService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MockBean
    private RandomCodeGenerator codeGenerator;

    @Autowired
    private DatabaseSetUp databaseSetUp;

    @BeforeEach
    void setUp() {
        databaseSetUp.clean();
        given(codeGenerator.generate(anyLong()))
                .willReturn(MOCKED_COUPON_VALUE);
    }

    @Test
    void createAndSend() {
        // given
        final String email = "foo@bar.com";

        // when
        couponService.createAndSend(1L, email);

        // then
        final Coupon saved = mongoTemplate.findOne(query(where("value").is(MOCKED_COUPON_VALUE)), Coupon.class);
        assertThat(saved).isNotNull();
    }
}
