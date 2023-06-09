package com.coupop.fcfscoupon.api.fcfs;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coupop.fcfscoupon.api.fcfs.dto.HistoryRequest;
import com.coupop.fcfscoupon.api.fcfs.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.fcfs.dto.ResendRequest;
import com.coupop.fcfscoupon.api.fcfs.support.RequestTime;
import com.coupop.fcfscoupon.client.coupon.CouponService;
import com.coupop.fcfscoupon.domain.fcfs.FcfsIssueService;
import com.coupop.fcfscoupon.domain.fcfs.exception.CouponNotOpenedException;
import com.coupop.fcfscoupon.domain.fcfs.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.domain.fcfs.exception.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.domain.history.HistoryService;
import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import com.coupop.fcfscoupon.domain.history.exception.HistoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
@Import(RequestTime.class)
class FcfsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FcfsIssueService fcfsIssueService;

    @MockBean
    private HistoryService historyService;

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
                .when(fcfsIssueService).issue(eq(email), any(LocalTime.class));

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
        final String email = "foo@bar.com";
        final IssuanceRequest request = new IssuanceRequest(email);

        doThrow(new CouponNotOpenedException())
                .when(fcfsIssueService).issue(eq(email), any(LocalTime.class));

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
        final String email = "foo@bar.com";
        final IssuanceRequest request = new IssuanceRequest(email);

        doThrow(new CouponOutOfStockException())
                .when(fcfsIssueService).issue(eq(email), any(LocalTime.class));

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
        final List<CouponIssueHistoryRecord> issueHistoryRecords = List.of(new CouponIssueHistoryRecord(
                "fakeid",
                "foo@bar.com",
                "fakeCouponId",
                LocalDateTime.of(2023, 5, 21, 10, 0))
        );

        given(historyService.findByEmail(eq(email)))
                .willReturn(issueHistoryRecords);

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

        doThrow(HistoryNotFoundException.ofEmail(email))
                .when(historyService).findByEmail(email);

        // when
        final ResultActions resultActions = performGet("/history", request);

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("title").value("쿠폰 발급 이력이 존재하지 않습니다."));
    }

    @DisplayName("발급된 쿠폰에 대해 재발송을 요청하면 같은 이메일로 재발송한 뒤 Accepted를 응답한다.")
    @Test
    void resend() throws Exception {
        // given
        final ResendRequest request = new ResendRequest("fakeId");

        // when
        final ResultActions resultActions = performPost("/resend", request);

        // then
        resultActions
                .andExpect(status().isAccepted());
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
