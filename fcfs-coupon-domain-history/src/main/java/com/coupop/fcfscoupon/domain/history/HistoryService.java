package com.coupop.fcfscoupon.domain.history;

import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import com.coupop.fcfscoupon.domain.history.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final CouponIssueHistoryRepository couponIssueHistoryRepository;

    public String create(final String email, final String couponId) {
        final CouponIssueHistory couponIssueHistory = CouponIssueHistory.ofNew(email, couponId);
        couponIssueHistoryRepository.save(couponIssueHistory);
        return couponIssueHistory.getId();
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

    public CouponIssueHistoryRecord findById(final String historyId) {
        final Optional<CouponIssueHistory> found = couponIssueHistoryRepository.findById(historyId);
        if (found.isEmpty()) {
            throw HistoryNotFoundException.ofId(historyId);
        }
        final CouponIssueHistory history = found.get();
        return CouponIssueHistoryRecord.of(history);
    }
}
