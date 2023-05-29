package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.coupop.fcfscoupon.api.fcfs.dto.HistoryRequest;
import com.coupop.fcfscoupon.api.fcfs.dto.HistoryResponse;
import com.coupop.fcfscoupon.api.fcfs.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.fcfs.dto.ResendRequest;
import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.api.fcfs.testconfig.IntegrationTestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalTime;
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
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

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
        final IssuanceRequest request = new IssuanceRequest(invalidEmail);

        // when
        final ValidatableResponse response = post("/issue", request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("형식에 맞는 이메일을 입력하세요."));
    }

    @DisplayName("쿠폰이 오픈되지 않았으면 쿠폰을 발급받을 수 없다.")
    @Test
    void issueCoupon_ifCouponIsNotOpen() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        final LocalTime closedTime = LocalTime.of(9, 59);
        given(requestTime.getValue())
                .willReturn(closedTime);

        // when
        final ValidatableResponse response = post("/issue", request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("쿠폰이 아직 오픈되지 않았습니다."));
    }

    @DisplayName("같은 이메일로 하루 두 번 쿠폰을 발급받을 수 없다.")
    @Test
    void issueCoupon_ifCouponUsedToday() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");
        post("/issue", request);

        // when
        final ValidatableResponse response = post("/issue", request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("이미 사용된 이메일입니다."));
    }

    @DisplayName("쿠폰이 소진되면 쿠폰을 발급받을 수 없다.")
    @Test
    void issueCoupon_ifCouponOutOfStock() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");
        databaseSetUp.setCount(FcfsIssuePolicy.getLimit());

        // when
        final ValidatableResponse response = post("/issue", request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("쿠폰이 모두 소진되었습니다."));
    }

    @DisplayName("이메일에 대해 쿠폰을 발급받은 이력을 조회한다.")
    @Test
    void findHistoryByEmail() {
        // given
        final String email = "foo@bar.com";
        post("/issue", new IssuanceRequest(email));

        final HistoryRequest request = new HistoryRequest(email);

        // when
        final ValidatableResponse response = get("/history", request);

        // then
        response.statusCode(OK.value())
                .body("issuedCoupons", hasSize(1))
                .body("issuedCoupons.date", contains(LocalDate.now().toString()));
    }

    @DisplayName("이메일에 대해 발급받은 쿠폰이 없을 경우 이력을 조회할 수 없다.")
    @Test
    void findHistoryByEmail_ifHistoryNotFound() {
        // given
        final HistoryRequest request = new HistoryRequest("foo@bar.com");

        // when
        final ValidatableResponse response = get("/history", request);

        // then
        response.statusCode(NOT_FOUND.value())
                .body("title", equalTo("쿠폰 발급 이력이 존재하지 않습니다."));
    }

    @DisplayName("발급된 쿠폰을 같은 이메일로 다시 전달받는다.")
    @Test
    void resend() {
        // given
        final String email = "foo@bar.com";
        post("/issue", new IssuanceRequest(email));
        final HistoryResponse history = get("/history", new HistoryRequest(email))
                .extract()
                .body()
                .as(HistoryResponse.class);
        final String historyId = history.issuedCoupons()
                .get(0)
                .id();

        final ResendRequest request = new ResendRequest(historyId);

        // when
        final ValidatableResponse response = post("resend", request);

        // then
        response.statusCode(ACCEPTED.value());
    }

    @DisplayName("해당하는 발급 이력이 존재하지 않으면 쿠폰을 재전송받을 수 없다.")
    @Test
    void resend_ifHistoryNotFound() {
        // given
        final ResendRequest request = new ResendRequest("invalidId");

        // when
        final ValidatableResponse response = post("resend", request);

        // then
        response.statusCode(NOT_FOUND.value())
                .body("title", equalTo("쿠폰 발급 이력이 존재하지 않습니다."));
    }

    private ValidatableResponse get(final String url, final Object request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .get(url)
                .then().log().all();
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
