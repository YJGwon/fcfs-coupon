package com.coupop.fcfscoupon.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CouponIssuePolicyTest {

    @DisplayName("쿠폰이 닫힌 시각인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"9,59,true", "10,0,false", "10,1,false"})
    void isCouponClosed(final int hour, final int minute, final boolean expected) {
        // given
        final LocalTime time = LocalTime.of(hour, minute);

        // when
        final boolean actual = CouponIssuePolicy.isCouponClosed(time);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
