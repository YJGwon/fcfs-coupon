package com.coupop.fcfscoupon.fcfsissue;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

import com.coupop.fcfscoupon.fcfsissue.dto.IssuanceRequest;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponNotOpenedException;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.fcfsissue.exception.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.fcfsissue.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.fcfsissue.support.RequestTime;
import com.coupop.fcfscoupon.testconfig.DatabaseSetUp;
import com.coupop.fcfscoupon.testconfig.MailSenderConfig;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(MailSenderConfig.class)
class FcfsIssueServiceTest {

    @Autowired
    private FcfsIssueService fcfsIssueService;

    @Autowired
    private DatabaseSetUp databaseSetUp;

    @MockBean
    private RequestTime requestTime;

    @BeforeEach
    void setUp() {
        databaseSetUp.clean();

        given(requestTime.getValue())
                .willReturn(FcfsIssuePolicy.getOpenAt());
    }

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void issue() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        // when & then
        assertThatNoException()
                .isThrownBy(() -> fcfsIssueService.issue(request));
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 열려있지 않으면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponIsNotOpen() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        final LocalTime closedTime = LocalTime.of(9, 59);
        given(requestTime.getValue())
                .willReturn(closedTime);

        // when & then
        assertThatExceptionOfType(CouponNotOpenedException.class)
                .isThrownBy(() -> fcfsIssueService.issue(request));
    }

    @DisplayName("쿠폰을 발급할 때 당일에 이미 사용된 이메일이면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifEmailUsedToday() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        fcfsIssueService.issue(request);

        // when & then
        assertThatExceptionOfType(EmailAlreadyUsedException.class)
                .isThrownBy(() -> fcfsIssueService.issue(request));
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 소진되면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponOutOfStock() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        databaseSetUp.setCount(FcfsIssuePolicy.getLimit());

        // when & then
        assertThatExceptionOfType(CouponOutOfStockException.class)
                .isThrownBy(() -> fcfsIssueService.issue(request));
    }
}
