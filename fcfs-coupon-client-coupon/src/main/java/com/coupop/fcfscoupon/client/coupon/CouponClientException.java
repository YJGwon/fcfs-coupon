package com.coupop.fcfscoupon.client.coupon;

import com.coupop.fcfscoupon.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class CouponClientException extends ApiException {

    public CouponClientException(final String title,
                                 final String detail,
                                 final HttpStatus httpStatus) {
        super(title, detail, httpStatus);
    }

    public static Mono<CouponClientException> of(final ClientResponse response) {
        final HttpStatus status = HttpStatus.resolve(response.statusCode().value());
        return response.bodyToMono(ProblemDetail.class)
                .flatMap(problemDetail -> Mono.error(
                        new CouponClientException(problemDetail.getTitle(), problemDetail.getDetail(), status))
                );
    }
}
