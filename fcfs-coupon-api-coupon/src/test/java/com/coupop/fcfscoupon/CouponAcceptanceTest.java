package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.testconfig.IntegrationTestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CouponAcceptanceTest extends IntegrationTestConfig {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("쿠폰을 발급받는다.")
    @Test
    void issueCoupon() {
        // given
        final IssuanceRequest request = new IssuanceRequest(1L, "foo@bar.com");

        // when
        final ValidatableResponse response = post("/issue", request);

        // then
        response.statusCode(ACCEPTED.value());
    }

    @DisplayName("형식에 맞지 않는 이메일을 입력하면 쿠폰을 발급받을 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"foobar.com", "foo@", "foo@com"})
    void issueCoupon_ifEmailInvalid(final String invalidEmail) {
        // given
        final IssuanceRequest request = new IssuanceRequest(1L, invalidEmail);

        // when
        final ValidatableResponse response = post("/issue", request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("형식에 맞는 이메일을 입력하세요."));
    }

    private ValidatableResponse post(final String url, final Object request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(url)
                .then().log().all();
    }
}
