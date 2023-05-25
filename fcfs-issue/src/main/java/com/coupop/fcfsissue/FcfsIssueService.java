package com.coupop.fcfsissue;

import com.coupop.fcfscoupon.domain.coupon.CouponService;
import com.coupop.fcfsissue.exception.CouponNotOpenedException;
import com.coupop.fcfsissue.exception.CouponOutOfStockException;
import com.coupop.fcfsissue.exception.EmailAlreadyUsedException;
import com.coupop.fcfsissue.model.FcfsIssuePolicy;
import com.coupop.fcfsissue.persistence.RedisFcfsIssueRepository;
import com.coupop.fcfsissue.support.TransactionalRedisOperations;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FcfsIssueService {

    private final CouponService couponService;

    private final RedisFcfsIssueRepository redisFcfsIssueRepository;
    private final TransactionalRedisOperations transactionalRedisOperations;

    public FcfsIssueService(final CouponService couponService,
                            final RedisFcfsIssueRepository redisFcfsIssueRepository,
                            final TransactionalRedisOperations transactionalRedisOperations) {
        this.couponService = couponService;
        this.redisFcfsIssueRepository = redisFcfsIssueRepository;
        this.transactionalRedisOperations = transactionalRedisOperations;
    }

    public void issue(final String email, final LocalTime requestTime) {
        checkCouponOpen(requestTime);
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

    private void checkCouponOpen(final LocalTime requestTime) {
        if (FcfsIssuePolicy.isCouponClosed(requestTime)) {
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
