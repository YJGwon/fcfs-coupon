package com.coupop.fcfscoupon.coupon;

import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.coupon.model.CouponRepository;
import com.coupop.fcfscoupon.coupon.model.RandomCodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final RandomCodeGenerator codeGenerator;
    private final CouponEmailSender couponEmailSender;

    public CouponService(final CouponRepository couponRepository,
                         final RandomCodeGenerator codeGenerator,
                         final CouponEmailSender couponEmailSender) {
        this.couponRepository = couponRepository;
        this.codeGenerator = codeGenerator;
        this.couponEmailSender = couponEmailSender;
    }

    public void createAndSend(final Long sequence, final String email) {
        final Coupon coupon = new Coupon(codeGenerator.generate(sequence));
        couponRepository.save(coupon);
        couponEmailSender.send(coupon, email);
    }
}
