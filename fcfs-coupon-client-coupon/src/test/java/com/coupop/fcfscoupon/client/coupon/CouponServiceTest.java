package com.coupop.fcfscoupon.client.coupon;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpRequest.request;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientAndServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = ClientAndServer.startClientAndServer(8081);
    }

    @AfterEach
    void stop() {
        mockServer.stop();
    }

    @DisplayName("설정된 url로 쿠폰 발급 요청을 전송한다.")
    @Test
    void issue() throws JsonProcessingException {
        // given
        final long seq = 1L;
        final String email = "foo@bar.com";

        // when
        couponService.issue(seq, email);

        // then
        mockServer.verify(
                request()
                        .withMethod("POST")
                        .withPath("/issue")
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withHeader("accept", MediaType.APPLICATION_JSON.toString())
                        .withBody(objectMapper.writeValueAsBytes(new IssuanceRequest(seq, email))),
                VerificationTimes.once()
        );
    }

    @DisplayName("설정된 url로 쿠폰 재발송 요청을 전송한다.")
    @Test
    void resend() throws JsonProcessingException {
        // given
        final String historyId = "fakeId";

        // when
        couponService.resend(historyId);

        // then
        mockServer.verify(
                request()
                        .withMethod("POST")
                        .withPath("/resend")
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withHeader("accept", MediaType.APPLICATION_JSON.toString())
                        .withBody(objectMapper.writeValueAsBytes(new ResendRequest(historyId))),
                VerificationTimes.once()
        );
    }

    @DisplayName("서버에서 에러를 응답할 경우 예외가 발생한다.")
    @Test
    void throwsException_whenServerResponseError() {
        // given
        final String errorMessage = "서버에 오류가 발생했습니다.";
        final String problemDetails = String.format("""
                {
                    "type" : "about:blank",
                    "title": "Internal Server Error",
                    "detail": "%s"
                }""", errorMessage);
        mockServer
                .when(request())
                .respond(
                        HttpResponse.response()
                                .withStatusCode(500)
                                .withHeader("Content-Type", "application/problem+json; charset=utf-8")
                                .withBody(problemDetails)
                );

        // when & then
        assertThatExceptionOfType(CouponClientException.class)
                .isThrownBy(() -> couponService.resend("fakeId"))
                .withMessage(errorMessage);
    }
}
