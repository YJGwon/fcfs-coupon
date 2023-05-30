package com.coupop.fcfscoupon.domain.coupon;

import com.coupop.fcfscoupon.domain.coupon.exception.CouponNotFoundException;
import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.domain.coupon.model.CouponRepository;
import com.coupop.fcfscoupon.domain.coupon.model.RandomCodeGenerator;
import com.coupop.fcfscoupon.domain.history.HistoryService;
import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final RandomCodeGenerator codeGenerator;
    private final CouponEmailSender couponEmailSender;

    private final CouponRepository couponRepository;
    private final HistoryService historyService;

    public CouponService(final RandomCodeGenerator codeGenerator,
                         final CouponEmailSender couponEmailSender,
                         final CouponRepository couponRepository,
                         final HistoryService historyService) {
        this.codeGenerator = codeGenerator;
        this.couponEmailSender = couponEmailSender;
        this.couponRepository = couponRepository;
        this.historyService = historyService;
    }

    public void createAndSend(final Long sequence, final String email) {
        final Coupon coupon = new Coupon(codeGenerator.generate(sequence));
        couponRepository.insert(coupon);
        historyService.create(email, coupon.getId());
        couponEmailSender.send(coupon, email);
    }

    public void resend(final String historyId) {
        final CouponIssueHistoryRecord history = historyService.findById(historyId);
        final String couponId = history.couponId();
        final Optional<Coupon> found = couponRepository.findById(couponId);
        if (found.isEmpty()) {
            throw new CouponNotFoundException(couponId);
        }
        final Coupon coupon = found.get();
        couponEmailSender.send(coupon, history.email());
    }
}
