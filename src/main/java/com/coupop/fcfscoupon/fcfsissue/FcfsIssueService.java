package com.coupop.fcfscoupon.fcfsissue;

import com.coupop.coupon.CouponService;
import com.coupop.fcfscoupon.fcfsissue.dto.IssuanceRequest;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponNotOpenedException;
import com.coupop.fcfscoupon.fcfsissue.exception.CouponOutOfStockException;
import com.coupop.fcfscoupon.fcfsissue.exception.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.fcfsissue.model.FcfsIssuePolicy;
import com.coupop.fcfscoupon.fcfsissue.persistence.RedisFcfsIssueRepository;
import com.coupop.fcfscoupon.fcfsissue.support.RequestTime;
import com.coupop.fcfscoupon.support.TransactionalRedisOperations;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FcfsIssueService {

    private final CouponService couponService;

    private final RedisFcfsIssueRepository redisFcfsIssueRepository;
    private final TransactionalRedisOperations transactionalRedisOperations;

    private final RequestTime requestTime;

    public FcfsIssueService(final CouponService couponService,
                            final RedisFcfsIssueRepository redisFcfsIssueRepository,
                            final TransactionalRedisOperations transactionalRedisOperations,
                            final RequestTime requestTime) {
        this.couponService = couponService;
        this.redisFcfsIssueRepository = redisFcfsIssueRepository;
        this.transactionalRedisOperations = transactionalRedisOperations;
        this.requestTime = requestTime;
    }

    public void issue(final IssuanceRequest request) {
        checkCouponOpen();
        final String email = request.email();
        final Long sequence = checkIssuableAndGetSequence(email);

        couponService.createAndSend(sequence, email);
    }

    private Long checkIssuableAndGetSequence(final String email) {
        final List<Object> result = transactionalRedisOperations.startSession()
                .multi()
                .andThen(ops -> redisFcfsIssueRepository.getCount(ops.opsForSet()))
                .andThen(ops -> redisFcfsIssueRepository.add(email))
                .exec();

        final Long count = (Long) result.get(0);
        checkCouponInStock(count.intValue(), email); // remove email

        final Long addResult = (Long) result.get(1);
        checkEmailNotUsedToday(addResult, email);
        return count;
    }

    private void checkCouponOpen() {
        if (FcfsIssuePolicy.isCouponClosed(requestTime.getValue())) {
            throw new CouponNotOpenedException();
        }
    }

    private void checkCouponInStock(final int count, final String email) {
        if (FcfsIssuePolicy.isCouponOutOfStock(count)) {
            redisFcfsIssueRepository.remove(email);
            throw new CouponOutOfStockException();
        }
    }

    private void checkEmailNotUsedToday(final Long result, final String email) {
        if (result.equals(0L)) {
            throw new EmailAlreadyUsedException(email);
        }
    }
}
