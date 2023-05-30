package com.coupop.fcfscoupon.domain.fcfs;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.coupop.fcfscoupon.client.coupon.CouponService;
import com.coupop.fcfscoupon.domain.fcfs.exception.CouponNotOpenedException;
import com.coupop.fcfscoupon.domain.fcfs.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.domain.fcfs.exception.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.domain.fcfs.testconfig.DataSetup;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class FcfsIssueServiceTest {

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

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void issue() {
        assertThatNoException()
                .isThrownBy(() -> fcfsIssueService.issue("foo@bar.com", OPENED_TIME));
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 열려있지 않으면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponIsNotOpen() {
        // given
        final LocalTime closedTime = OPENED_TIME.minusMinutes(1L);

        // when & then
        assertThatExceptionOfType(CouponNotOpenedException.class)
                .isThrownBy(() -> fcfsIssueService.issue("foo@bar.com", closedTime));
    }

    @DisplayName("쿠폰을 발급할 때 당일에 이미 사용된 이메일이면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifEmailUsedToday() {
        // given
        final String email = "foo@bar.com";
        fcfsIssueService.issue(email, OPENED_TIME);

        // when & then
        assertThatExceptionOfType(EmailAlreadyUsedException.class)
                .isThrownBy(() -> fcfsIssueService.issue(email, OPENED_TIME));
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 소진되면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponOutOfStock() {
        // given
        dataSetup.setCount(FcfsIssuePolicy.getLimit());

        // when & then
        assertThatExceptionOfType(CouponOutOfStockException.class)
                .isThrownBy(() -> fcfsIssueService.issue("foo@bar.com", OPENED_TIME));
    }
}
