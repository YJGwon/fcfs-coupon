package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.coupon.CouponService;
import com.coupop.fcfscoupon.coupon.dto.HistoryRequest;
import com.coupop.fcfscoupon.coupon.dto.HistoryResponse;
import com.coupop.fcfscoupon.exception.ApiException;
import com.coupop.fcfscoupon.fcfsissue.FcfsIssueService;
import com.coupop.fcfscoupon.fcfsissue.dto.IssuanceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController {

    private final FcfsIssueService fcfsIssueService;
    private final CouponService couponService;

    public CouponController(final FcfsIssueService fcfsIssueService,
                            final CouponService couponService) {
        this.fcfsIssueService = fcfsIssueService;
        this.couponService = couponService;
    }

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issue(@RequestBody @Validated final IssuanceRequest request) {
        fcfsIssueService.issue(request);
    }

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public HistoryResponse findHistoryByEmail(@RequestBody @Validated final HistoryRequest request) {
        return couponService.findHistoryByEmail(request);
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
