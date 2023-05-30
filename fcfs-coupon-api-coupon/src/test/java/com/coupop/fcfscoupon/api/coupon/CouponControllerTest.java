package com.coupop.fcfscoupon.api.coupon;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import com.coupop.fcfscoupon.domain.coupon.CouponService;
import com.coupop.fcfscoupon.domain.coupon.exception.CouponNotFoundException;
import com.coupop.fcfscoupon.domain.history.exception.HistoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @DisplayName("쿠폰 발행에 성공하면 Accepted 상태를 반환한다.")
    @Test
    void issue() throws Exception {
        // given
        final IssuanceRequest request = new IssuanceRequest(1L, "foo@bar.com");

        // when
        final ResultActions resultActions = performPost("/issue", request);

        // then
        resultActions
                .andExpect(status().isAccepted());
    }

    @DisplayName("쿠폰 발행시, 이메일이 형식에 맞지 않으면 Bad Request 상태를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"foobar.com", "foo@", "foo@com"})
    void issue_responseError_ifEmailInvalid(final String invalidEmail) throws Exception {
        // given
        final IssuanceRequest request = new IssuanceRequest(1L, invalidEmail);

        // when
        final ResultActions resultActions = performPost("/issue", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("형식에 맞는 이메일을 입력하세요."));
    }

    @DisplayName("쿠폰 재발송에 성공하면 Accepted 상태를 반환한다.")
    @Test
    void send() throws Exception {
        // given
        final ResendRequest request = new ResendRequest("fakeId");

        // when
        final ResultActions resultActions = performPost("/resend", request);

        // then
        resultActions
                .andExpect(status().isAccepted());
    }

    @DisplayName("쿠폰 재발송시, 쿠폰이 존재하지 않으면 Not Found 상태를 반환한다.")
    @Test
    void send_responseError_ifCouponNotFound() throws Exception {
        // given
        final String historyId = "fakeId";
        final ResendRequest request = new ResendRequest(historyId);
        doThrow(new CouponNotFoundException("invalidId"))
                .when(couponService)
                .resend(historyId);

        // when
        final ResultActions resultActions = performPost("/resend", request);

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("title").value("쿠폰이 존재하지 않습니다."));
    }

    @DisplayName("쿠폰 재발송시, 발급 이력이 존재하지 않으면 Not Found 상태를 반환한다.")
    @Test
    void send_responseError_ifHistoryNotFound() throws Exception {
        // given
        final String historyId = "invalidId";
        final ResendRequest request = new ResendRequest(historyId);
        doThrow(HistoryNotFoundException.ofId("invalidId"))
                .when(couponService)
                .resend(historyId);

        // when
        final ResultActions resultActions = performPost("/resend", request);

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("title").value("쿠폰 발급 이력이 존재하지 않습니다."));
    }

    private ResultActions performPost(final String url, final Object request) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }
}
