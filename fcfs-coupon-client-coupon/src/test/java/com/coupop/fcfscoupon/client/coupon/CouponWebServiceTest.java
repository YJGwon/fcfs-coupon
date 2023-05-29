package com.coupop.fcfscoupon.client.coupon;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest
class CouponWebServiceTest {

    private CouponWebService couponWebService;

    @BeforeEach
    void setUp() {
        final MockWebServer mockWebServer = startServer();

        final WebClient client = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
                .build();
        couponWebService = factory.createClient(CouponWebService.class);
    }

    @DisplayName("설정된 url로 발급 요청을 전송한다.")
    @Test
    void issue() {
        couponWebService.issue(1L, "foo@bar.com");
    }

    private MockWebServer startServer() {
        final MockWebServer mockWebServer = new MockWebServer();
        final MockResponse issueResponse = new MockResponse()
                .setResponseCode(HttpStatus.ACCEPTED.value());
        final Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull final RecordedRequest recordedRequest) throws InterruptedException {
                if (recordedRequest.getPath().contains("/issue")) {
                    return issueResponse;
                }
                return new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value());
            }
        };
        mockWebServer.setDispatcher(dispatcher);
        return mockWebServer;
    }
}
