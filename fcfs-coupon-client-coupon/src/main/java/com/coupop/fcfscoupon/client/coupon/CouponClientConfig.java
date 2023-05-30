package com.coupop.fcfscoupon.client.coupon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class CouponClientConfig {

    @Bean
    CouponWebService couponWebService() {
        final WebClient client = WebClient.builder()
                .defaultStatusHandler(HttpStatusCode::isError, CouponClientException::of)
                .baseUrl("http://localhost:8081")
                .build();
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
                .build();
        return factory.createClient(CouponWebService.class);
    }
}
