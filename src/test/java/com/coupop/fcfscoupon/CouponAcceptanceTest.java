package com.coupop.fcfscoupon;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.coupop.fcfscoupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.testconfig.DatabaseSetUp;
import com.coupop.fcfscoupon.testconfig.MailSenderConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.time.LocalTime;
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
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(MailSenderConfig.class)
public class CouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseSetUp databaseSetUp;

    @MockBean
    private RequestTime requestTime;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseSetUp.clean();

        given(requestTime.getValue())
                .willReturn(FcfsIssuePolicy.getOpenAt());
    }

    @DisplayName("쿠폰을 발급받는다.")
    @Test
    void issueCoupon() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");

        // when
        final ValidatableResponse response = post(request);

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
        final ValidatableResponse response = post(request);

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
        final ValidatableResponse response = post(request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("쿠폰이 아직 오픈되지 않았습니다."));
    }

    @DisplayName("같은 이메일로 하루 두 번 쿠폰을 발급받을 수 없다.")
    @Test
    void issueCoupon_ifCouponUsedToday() {
        // given
        final IssuanceRequest request = new IssuanceRequest("foo@bar.com");
        post(request);

        // when
        final ValidatableResponse response = post(request);

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
        final ValidatableResponse response = post(request);

        // then
        response.statusCode(BAD_REQUEST.value())
                .body("title", equalTo("쿠폰이 모두 소진되었습니다."));
    }

    private ValidatableResponse post(final IssuanceRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/issue")
                .then().log().all();
    }
}
