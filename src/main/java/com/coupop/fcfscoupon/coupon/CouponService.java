package com.coupop.fcfscoupon.coupon;

import com.coupop.fcfscoupon.coupon.dto.HistoryRequest;
import com.coupop.fcfscoupon.coupon.dto.HistoryResponse;
import com.coupop.fcfscoupon.coupon.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryDetail;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryRepository;
import com.coupop.fcfscoupon.coupon.model.CouponRepository;
import com.coupop.fcfscoupon.coupon.model.RandomCodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final RandomCodeGenerator codeGenerator;
    private final CouponEmailSender couponEmailSender;

    private final CouponRepository couponRepository;
    private final CouponIssueHistoryRepository couponIssueHistoryRepository;

    public CouponService(final RandomCodeGenerator codeGenerator,
                         final CouponEmailSender couponEmailSender,
                         final CouponRepository couponRepository,
                         final CouponIssueHistoryRepository couponIssueHistoryRepository) {
        this.codeGenerator = codeGenerator;
        this.couponEmailSender = couponEmailSender;
        this.couponRepository = couponRepository;
        this.couponIssueHistoryRepository = couponIssueHistoryRepository;
    }

    public void createAndSend(final Long sequence, final String email) {
        final Coupon coupon = new Coupon(codeGenerator.generate(sequence));
        couponRepository.save(coupon);
        couponIssueHistoryRepository.save(email, CouponIssueHistoryDetail.ofNew(coupon));
        couponEmailSender.send(coupon, email);
    }

    public HistoryResponse findHistoryByEmail(final HistoryRequest request) {
        final String email = request.email();
        final CouponIssueHistory history = couponIssueHistoryRepository.findByEmail(email);
        if (history == null) {
            throw new HistoryNotFoundException(email);
        }
        return HistoryResponse.of(history);
    }
}
