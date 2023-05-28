package com.coupop.fcfscoupon.domain.history;

import com.coupop.fcfscoupon.client.coupon.CouponWebService;
import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import com.coupop.fcfscoupon.domain.history.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private final CouponWebService couponWebService;
    private final CouponIssueHistoryRepository couponIssueHistoryRepository;

    public HistoryService(final CouponWebService couponWebService,
                          final CouponIssueHistoryRepository couponIssueHistoryRepository) {
        this.couponWebService = couponWebService;
        this.couponIssueHistoryRepository = couponIssueHistoryRepository;
    }

    public void create(final String email, final String couponId) {
        couponIssueHistoryRepository.save(CouponIssueHistory.ofNew(email, couponId));
    }

    public List<CouponIssueHistoryRecord> findByEmail(final String email) {
        final List<CouponIssueHistory> histories = couponIssueHistoryRepository.findByEmail(email);
        if (histories.isEmpty()) {
            throw HistoryNotFoundException.ofEmail(email);
        }
        return histories.stream()
                .map(CouponIssueHistoryRecord::of)
                .toList();
    }

    public void resend(final String historyId) {
        final Optional<CouponIssueHistory> found = couponIssueHistoryRepository.findById(historyId);
        if (found.isEmpty()) {
            throw HistoryNotFoundException.ofId(historyId);
        }
        final CouponIssueHistory history = found.get();
        couponWebService.send(history.getCouponId(), history.getEmail());
    }
}
