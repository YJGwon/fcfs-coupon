package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import com.coupop.fcfscoupon.api.coupon.testconfig.DataSetup;
import com.coupop.fcfscoupon.domain.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CouponApplication.class)
public class CouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DataSetup dataSetup;

    @MockBean
    private CouponEmailSender couponEmailSender;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        dataSetup.clean();
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

    @DisplayName("발급 이력에 따라 쿠폰을 발송한다.")
    @Test
    void resendCoupon() {
        // given
        final CouponIssueHistory history = dataSetup.addHistory(dataSetup.addCoupon().getId());
        final ResendRequest request = new ResendRequest(history.getId());

        // when
        final ValidatableResponse response = post("/resend", request);

        // then
        response.statusCode(ACCEPTED.value());
    }

    @DisplayName("쿠폰이 존재하지 않으면 쿠폰을 재발송할 수 없다.")
    @Test
    void resendCoupon_ifCouponNotFound() {
        // given
        final CouponIssueHistory history = dataSetup.addHistory("invalidId");
        final ResendRequest request = new ResendRequest(history.getId());

        // when
        final ValidatableResponse response = post("/resend", request);

        // then
        response.statusCode(NOT_FOUND.value())
                .body("title", equalTo("쿠폰이 존재하지 않습니다."));
    }

    @DisplayName("발급 이력이 없으면 쿠폰을 재발송할 수 없다.")
    @Test
    void resendCoupon_ifHistoryNotFound() {
        // given
        final ResendRequest request = new ResendRequest("invalidId");

        // when
        final ValidatableResponse response = post("/resend", request);

        // then
        response.statusCode(NOT_FOUND.value())
                .body("title", equalTo("쿠폰 발급 이력이 존재하지 않습니다."));
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
