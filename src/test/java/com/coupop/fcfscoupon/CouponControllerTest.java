package com.coupop.fcfscoupon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coupop.fcfscoupon.fcfsissue.FcfsIssueService;
import com.coupop.fcfscoupon.fcfsissue.dto.IssuanceRequest;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponNotOpenedException;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.fcfsissue.exception.EmailAlreadyUsedException;
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
    private FcfsIssueService fcfsIssueService;

    @DisplayName("쿠폰 발행에 성공하면 쿠폰 내용과 함께 Accepted 상태를 반환한다.")
    @Test
    void issue() throws Exception {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        // when
        final ResultActions resultActions = mockMvc.perform(post("/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

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
        final ResultActions resultActions = mockMvc.perform(post("/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

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
        final ResultActions resultActions = mockMvc.perform(post("/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

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
        final ResultActions resultActions = mockMvc.perform(post("/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

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
        final ResultActions resultActions = mockMvc.perform(post("/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("쿠폰이 모두 소진되었습니다."));
    }
}
