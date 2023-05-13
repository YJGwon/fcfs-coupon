package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.ApiException;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.execption.CouponOutOfStockException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
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
    public CouponResponse issue() {
        return couponService.issue();
    }

    @ExceptionHandler({CouponNotOpenedException.class, CouponOutOfStockException.class})
    public ErrorResponse handleCouponNotOpenedException(final ApiException e) {
        return ErrorResponse.builder(e, e.getHttpStatus(), e.getMessage())
                .type(e.getType())
                .title(e.getTitle())
                .build();
    }
}
