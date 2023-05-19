package com.coupop.fcfscoupon.coupon.persistence;

import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.CouponRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCouponRepository extends CouponRepository, MongoRepository<Coupon, String> {

    @Override
    default Coupon save(final Coupon coupon) {
        return this.insert(coupon);
    }
}
