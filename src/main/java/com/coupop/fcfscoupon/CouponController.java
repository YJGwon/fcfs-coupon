package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponRequest;
import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.ApiException;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse issue(@RequestBody @Validated final CouponRequest request) {
        return couponService.issue(request);
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
}
