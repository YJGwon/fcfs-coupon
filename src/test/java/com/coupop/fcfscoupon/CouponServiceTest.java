package com.coupop.fcfscoupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

import com.coupop.fcfscoupon.dto.CouponRequest;
import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.execption.CouponOutOfStockException;
import com.coupop.fcfscoupon.model.CouponCountRepository;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponCountRepository couponCountRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @MockBean
    private RequestTime requestTime;

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();

        given(requestTime.getValue())
                .willReturn(CouponIssuePolicy.getOpenAt());
    }

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void issue() {
        // given
        final CouponRequest request = new CouponRequest("foo@bar.com");

        // when
        final CouponResponse response = couponService.issue(request);

        // then
        assertThat(response.value()).isNotBlank();
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 열려있지 않으면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponIsNotOpen() {
        // given
        final CouponRequest request = new CouponRequest("foo@bar.com");

        final LocalTime closedTime = LocalTime.of(9, 59);
        given(requestTime.getValue())
                .willReturn(closedTime);

        // when & then
        assertThatExceptionOfType(CouponNotOpenedException.class)
                .isThrownBy(() -> couponService.issue(request));
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 소진되면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponOutOfStock() {
        // given
        final CouponRequest request = new CouponRequest("foo@bar.com");

        couponCountRepository.setCount(CouponIssuePolicy.getLimit());

        // when & then
        assertThatExceptionOfType(CouponOutOfStockException.class)
                .isThrownBy(() -> couponService.issue(request));
    }
}
