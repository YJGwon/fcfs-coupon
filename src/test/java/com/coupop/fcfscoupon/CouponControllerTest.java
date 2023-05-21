package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coupop.fcfscoupon.coupon.CouponService;
import com.coupop.fcfscoupon.coupon.dto.HistoryRequest;
import com.coupop.fcfscoupon.coupon.dto.HistoryResponse;
import com.coupop.fcfscoupon.coupon.dto.IssuedCouponResponse;
import com.coupop.fcfscoupon.coupon.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.fcfsissue.FcfsIssueService;
import com.coupop.fcfscoupon.fcfsissue.dto.IssuanceRequest;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponNotOpenedException;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.fcfsissue.exception.EmailAlreadyUsedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
    private FcfsIssueService fcfsIssueService;

    @MockBean
    private CouponService couponService;

    @DisplayName("쿠폰 발행에 성공하면 Accepted 상태를 반환한다.")
    @Test
    void issue() throws Exception {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

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
        final IssuanceRequest request = new IssuanceRequest(invalidEmail);

        // when
        final ResultActions resultActions = performPost("/issue", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("형식에 맞는 이메일을 입력하세요."));
    }

    @DisplayName("쿠폰 발행시, 당일에 이미 사용된 이메일이면 Bad Request 상태를 반환한다.")
    @Test
    void issue_responseError_ifEmailUsedToday() throws Exception {
        // given
        final String email = "foo@bar.com";
        final IssuanceRequest request = new IssuanceRequest(email);

        doThrow(new EmailAlreadyUsedException(email))
                .when(fcfsIssueService).issue(any(IssuanceRequest.class));

        // when
        final ResultActions resultActions = performPost("/issue", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("이미 사용된 이메일입니다."));
    }

    @DisplayName("쿠폰 발행시, 쿠폰이 오픈되지 않았으면 Bad Request 상태를 반환한다.")
    @Test
    void issue_responseError_ifCouponIsNotOpen() throws Exception {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        doThrow(new CouponNotOpenedException())
                .when(fcfsIssueService).issue(any(IssuanceRequest.class));

        // when
        final ResultActions resultActions = performPost("/issue", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("쿠폰이 아직 오픈되지 않았습니다."));
    }

    @DisplayName("쿠폰 발행시, 쿠폰 재고가 소진되었으면 Bad Request 상태를 반환한다.")
    @Test
    void issue_responseError_ifCouponOutOfStock() throws Exception {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        doThrow(new CouponOutOfStockException())
                .when(fcfsIssueService).issue(any(IssuanceRequest.class));

        // when
        final ResultActions resultActions = performPost("/issue", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("쿠폰이 모두 소진되었습니다."));
    }

    @DisplayName("이메일에 대한 쿠폰 발급 이력 조회에 성공하면 발급된 쿠폰 목록과 함께 OK 상태를 반환한다.")
    @Test
    void findHistoryByEmail() throws Exception {
        // given
        final String email = "foo@bar.com";
        final HistoryRequest request = new HistoryRequest(email);
        final HistoryResponse response = new HistoryResponse(
                List.of(new IssuedCouponResponse("fakeid", "2023-05-21")));

        given(couponService.findHistoryByEmail(any(HistoryRequest.class)))
                .willReturn(response);
        // when
        final ResultActions resultActions = performGet("/history", request);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issuedCoupons", hasSize(1)))
                .andExpect(jsonPath("$.issuedCoupons[*].id").value("fakeid"))
                .andExpect(jsonPath("$.issuedCoupons[*].date").value("2023-05-21"));
    }

    @DisplayName("이메일에 대한 쿠폰 발급 이력 조회시, 발급 이력이 존재하지 않으면 Not Found 상태를 반환한다.")
    @Test
    void findHistoryByEmail_ifHistoryNotFound() throws Exception {
        // given
        final String email = "foo@bar.com";
        final HistoryRequest request = new HistoryRequest(email);

        doThrow(new HistoryNotFoundException(email))
                .when(couponService).findHistoryByEmail(any(HistoryRequest.class));

        // when
        final ResultActions resultActions = performGet("/history", request);

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("title").value("쿠폰 발급 이력이 존재하지 않습니다."));
    }

    private ResultActions performGet(final String url, final Object request) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions performPost(final String url, final Object request) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }
}
