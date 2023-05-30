package com.coupop.fcfscoupon.domain.coupon;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.coupop.fcfscoupon.domain.coupon.exception.CouponNotFoundException;
import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.domain.coupon.model.RandomCodeGenerator;
import com.coupop.fcfscoupon.domain.coupon.testconfig.DataSetup;
import com.coupop.fcfscoupon.domain.history.HistoryService;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class CouponServiceTest {
    protected static final String MOCKED_COUPON_VALUE = "fakevalue";

    @Autowired
    private CouponService couponService;

    @Autowired
    private DataSetup dataSetup;

    @SpyBean
    private HistoryService historyService;

    @MockBean
    private RandomCodeGenerator codeGenerator;

    @MockBean
    private CouponEmailSender couponEmailSender;

    @BeforeEach
    void setUp() {
        dataSetup.clean();

        given(codeGenerator.generate(anyLong()))
                .willReturn(MOCKED_COUPON_VALUE);
    }

    @DisplayName("쿠폰을 생성하여 저장하고 쿠폰 발송 내역을 저장한 뒤 이메일을 발송한다.")
    @Test
    void createAndSend() {
        // given
        final String email = "foo@bar.com";

        // when
        couponService.createAndSend(1L, email);

        // then
        assertAll(
                () -> verify(historyService).create(eq(email), anyString()),
                () -> verify(couponEmailSender).send(any(Coupon.class), eq(email))
        );
    }

    @DisplayName("이미 발급된 쿠폰에 대해 재전송을 요청하면 같은 이메일로 재전송한다.")
    @Test
    void resend() {
        // given
        final String email = "foo@bar.com";
        final CouponIssueHistory history = dataSetup.addHistory(dataSetup.addCoupon().getId(), email);

        // when
        couponService.resend(history.getId());

        // then
        verify(couponEmailSender).send(any(Coupon.class), eq(email));
    }

    @DisplayName("재전송을 요청할 때 해당하는 쿠폰이 없을 경우 예외가 발생한다.")
    @Test
    void send_ifCouponNotFound() {
        // given
        final String email = "foo@bar.com";
        final CouponIssueHistory history = dataSetup.addHistory("invalidId", email);

        // when & then
        assertThatExceptionOfType(CouponNotFoundException.class)
                .isThrownBy(() -> couponService.resend(history.getId()));
    }
}
