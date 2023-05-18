package com.coupop.fcfscoupon;

import com.coupop.fcfscoupon.dto.CouponRequest;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import com.coupop.fcfscoupon.execption.CouponOutOfStockException;
import com.coupop.fcfscoupon.execption.EmailAlreadyUsedException;
import com.coupop.fcfscoupon.model.Coupon;
import com.coupop.fcfscoupon.model.CouponEmailSender;
import com.coupop.fcfscoupon.model.CouponIssuePolicy;
import com.coupop.fcfscoupon.model.RandomCodeGenerator;
import com.coupop.fcfscoupon.percistence.RedisCouponIssuanceRepository;
import com.coupop.fcfscoupon.percistence.TransactionalRedisOperations;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final RedisCouponIssuanceRepository couponIssuanceRepository;
    private final TransactionalRedisOperations transactionalRedisOperations;
    private final CouponEmailSender couponEmailSender;

    private final RequestTime requestTime;
    private final RandomCodeGenerator codeGenerator;

    public CouponService(final RedisCouponIssuanceRepository couponIssuanceRepository,
                         final TransactionalRedisOperations transactionalRedisOperations,
                         final CouponEmailSender couponEmailSender,
                         final RequestTime requestTime,
                         final RandomCodeGenerator codeGenerator) {
        this.couponIssuanceRepository = couponIssuanceRepository;
        this.transactionalRedisOperations = transactionalRedisOperations;
        this.couponEmailSender = couponEmailSender;
        this.requestTime = requestTime;
        this.codeGenerator = codeGenerator;
    }

    public void issue(final CouponRequest request) {
        checkCouponOpen();
        final String email = request.email();
        final List<Object> result = transactionalRedisOperations.startSession()
                .multi()
                .andThen(ops -> couponIssuanceRepository.getCount(ops.opsForSet()))
                .andThen(ops -> couponIssuanceRepository.add(email))
                .exec();

        final Long count = (Long) result.get(0);
        checkCouponInStock(count.intValue(), email); // remove email

        final Long addResult = (Long) result.get(1);
        checkEmailNotUsedToday(addResult, email);

        final Coupon coupon = new Coupon(codeGenerator.generate(count));
        couponEmailSender.send(coupon, email);
    }

    private void checkCouponOpen() {
        if (CouponIssuePolicy.isCouponClosed(requestTime.getValue())) {
            throw new CouponNotOpenedException();
        }
    }

    private void checkCouponInStock(final int count, final String email) {
        if (CouponIssuePolicy.isCouponOutOfStock(count)) {
            couponIssuanceRepository.remove(email);
            throw new CouponOutOfStockException();
        }
    }

    private void checkEmailNotUsedToday(final Long result, final String email) {
        if (result.equals(0L)) {
            throw new EmailAlreadyUsedException(email);
        }
    }
}
