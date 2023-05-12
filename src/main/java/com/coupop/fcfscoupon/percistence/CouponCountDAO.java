package com.coupop.fcfscoupon.percistence;

import com.coupop.fcfscoupon.model.CouponCountRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CouponCountDAO implements CouponCountRepository {

    private int count = 0;

    @Override
    public void increaseCount() {
        count++;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(final int count) {
        this.count = count;
    }
}
