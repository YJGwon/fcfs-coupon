package com.coupop.fcfscoupon.api.coupon;

import com.coupop.fcfscoupon.api.coupon.dto.IssuanceRequest;
import com.coupop.fcfscoupon.api.coupon.dto.ResendRequest;
import com.coupop.fcfscoupon.common.exception.ApiException;
import com.coupop.fcfscoupon.domain.coupon.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issue(@RequestBody @Validated final IssuanceRequest request) {
        couponService.createAndSend(request.seq(), request.email());
    }

    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resend(@RequestBody @Validated final ResendRequest request) {
        couponService.resend(request.historyId());
    }

    @ExceptionHandler(ApiException.class)
    public ErrorResponse handleApiExceptions(final ApiException e) {
        return ErrorResponse.builder(e, e.getHttpStatus(), e.getMessage())
                .type(e.getType())
                .title(e.getTitle())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(final MethodArgumentNotValidException e) {
        final FieldError fieldError = e.getFieldError();
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, fieldError.getRejectedValue().toString())
                .type(e.getBody().getType())
                .title(fieldError.getDefaultMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleServerExceptions(final Exception e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");
    }
}
