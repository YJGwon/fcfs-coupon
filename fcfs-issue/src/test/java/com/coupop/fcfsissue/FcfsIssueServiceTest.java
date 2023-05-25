package com.coupop.fcfsissue;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.coupop.fcfsissue.exception.CouponNotOpenedException;
import com.coupop.fcfsissue.exception.CouponOutOfStockException;
import com.coupop.fcfsissue.exception.EmailAlreadyUsedException;
import com.coupop.fcfsissue.model.FcfsIssuePolicy;
import com.coupop.fcfsissue.testconfig.IntegrationTestConfig;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FcfsIssueServiceTest extends IntegrationTestConfig {

    private static final LocalTime OPENED_TIME = FcfsIssuePolicy.getOpenAt();

    @Autowired
    private FcfsIssueService fcfsIssueService;

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
        databaseSetUp.setCount(FcfsIssuePolicy.getLimit());

        // when & then
        assertThatExceptionOfType(CouponOutOfStockException.class)
                .isThrownBy(() -> fcfsIssueService.issue("foo@bar.com", OPENED_TIME));
    }
}
