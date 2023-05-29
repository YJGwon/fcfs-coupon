package com.coupop.fcfscoupon.api.coupon.testconfig;

import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final MongoTemplate mongoTemplate;
    private final CouponRepository couponRepository;

    public DataSetup(final MongoTemplate mongoTemplate, final CouponRepository couponRepository) {
        this.mongoTemplate = mongoTemplate;
        this.couponRepository = couponRepository;
    }

    public void clean() {
        for (String collectionName : mongoTemplate.getCollectionNames()) {
            mongoTemplate.dropCollection(collectionName);
        }
    }

    public Coupon addCoupon() {
        final Coupon coupon = new Coupon("fakeValue");
        couponRepository.insert(coupon);
        return coupon;
    }
}
