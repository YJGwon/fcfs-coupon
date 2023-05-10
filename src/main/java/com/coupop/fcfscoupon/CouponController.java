package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponResponse;
import org.springframework.http.HttpStatus;
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
}
