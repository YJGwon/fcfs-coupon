package com.coupop.fcfscoupon.domain.coupon;

import com.coupop.fcfscoupon.domain.coupon.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.domain.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.coupon.model.CouponIssueHistoryRepository;
import com.coupop.fcfscoupon.domain.coupon.model.CouponRepository;
import com.coupop.fcfscoupon.domain.coupon.model.RandomCodeGenerator;
import java.util.List;
import java.util.Optional;
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
        couponIssueHistoryRepository.save(CouponIssueHistory.ofNew(email, coupon));
        couponEmailSender.send(coupon, email);
    }

    public <T> T findHistoryByEmail(final String email,
                                    final CouponIssueHistoriesResponseConverter<T> responseConverter) {
        final List<CouponIssueHistory> histories = couponIssueHistoryRepository.findByEmail(email);
        if (histories.isEmpty()) {
            throw HistoryNotFoundException.ofEmail(email);
        }
        return responseConverter.convert(histories);
    }

    public void resend(final String historyId) {
        final Optional<CouponIssueHistory> found = couponIssueHistoryRepository.findById(historyId);
        if (found.isEmpty()) {
            throw HistoryNotFoundException.ofId(historyId);
        }
        final CouponIssueHistory history = found.get();
        couponEmailSender.send(history.getCoupon(), history.getEmail());
    }
}
