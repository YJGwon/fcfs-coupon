package com.coupop.fcfscoupon.client.coupon;

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
        final long seq = 1L;
        final String email = "foo@bar.com";
        couponService.issue(seq, email);
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
        final String historyId = "fakeId";
        couponService.resend(historyId);
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
}
