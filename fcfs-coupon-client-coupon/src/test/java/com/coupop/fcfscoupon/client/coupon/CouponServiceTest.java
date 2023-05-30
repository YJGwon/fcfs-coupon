package com.coupop.fcfscoupon.client.coupon;

import java.io.IOException;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest
class CouponServiceTest {

    private CouponService couponService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void startMockServer() {
        mockWebServer = startServer();

        final WebClient client = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
                .build();
        couponService = new CouponService(factory.createClient(CouponWebService.class));
    }

    @AfterEach
    void closeMockServer() throws IOException {
        mockWebServer.close();
    }

    @DisplayName("설정된 url로 쿠폰 발급 요청을 전송한다.")
    @Test
    void issue() {
        couponService.issue(1L, "foo@bar.com");
    }

    @DisplayName("설정된 url로 쿠폰 재발송 요청을 전송한다.")
    @Test
    void resend() {
        couponService.resend("fakeId");
    }

    private MockWebServer startServer() {
        final MockWebServer mockWebServer = new MockWebServer();
        final MockResponse acceptedResponse = new MockResponse()
                .setResponseCode(HttpStatus.ACCEPTED.value());
        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull final RecordedRequest recordedRequest) {
                if (recordedRequest.getPath().contains("/issue")) {
                    return acceptedResponse;
                }
                if (recordedRequest.getPath().contains("/resend")) {
                    return acceptedResponse;
                }
                return new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value());
            }
        };
        mockWebServer.setDispatcher(dispatcher);
        return mockWebServer;
    }
}
