package com.coupop.fcfscoupon.domain.coupon.persistence;

import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCouponRepository extends CouponRepository, MongoRepository<Coupon, String> {
}
