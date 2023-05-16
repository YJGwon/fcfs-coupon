package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import com.coupop.fcfscoupon.dto.CouponRequest;
import com.coupop.fcfscoupon.model.CouponCountRepository;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CouponCountRepository couponCountRepository;

    @MockBean
    private RequestTime requestTime;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        couponCountRepository.setCount(0);

        given(requestTime.getValue())
                .willReturn(CouponIssuePolicy.getOpenAt());
    }

    @DisplayName("쿠폰을 발급받는다.")
    @Test
    void issueCoupon() {
        // given
        final String path = "/issue";
        final CouponRequest request = new CouponRequest("foo@bar.com");

        // when
        final ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(path)
                .then().log().all();

        // then
        response.statusCode(CREATED.value())
                .body("value", equalTo("뭔가 좋은 쿠폰"));
    }

    @DisplayName("쿠폰이 오픈되지 않았으면 쿠폰을 발급받을 수 없다.")
    @Test
    void issueCoupon_ifCouponIsNotOpen() {
        // given
        final String path = "/issue";
        final CouponRequest request = new CouponRequest("foo@bar.com");

        final LocalTime closedTime = LocalTime.of(9, 59);
        given(requestTime.getValue())
                .willReturn(closedTime);

        // when
        final ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(path)
                .then().log().all();

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("쿠폰이 아직 오픈되지 않았습니다."));
    }

    @DisplayName("쿠폰이 소진되면 쿠폰을 발급받을 수 없다.")
    @Test
    void issueCoupon_ifCouponOutOfStock() {
        // given
        final String path = "/issue";
        final CouponRequest request = new CouponRequest("foo@bar.com");

        couponCountRepository.setCount(CouponIssuePolicy.getLimit());

        // when
        final ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(path)
                .then().log().all();

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("쿠폰이 모두 소진되었습니다."));
    }
}
