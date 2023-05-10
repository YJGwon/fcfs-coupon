package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CouponAcceptanceTest {

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
        final String path = "/issue";

        // when
        final ValidatableResponse response = RestAssured
                .given().log().all()
                .when()
                .post(path)
                .then().log().all();

        // then
        response.statusCode(CREATED.value())
                .body("value", equalTo("뭔가 좋은 쿠폰"));
    }
}
