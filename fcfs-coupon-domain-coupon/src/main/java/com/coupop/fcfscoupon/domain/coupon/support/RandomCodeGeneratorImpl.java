package com.coupop.fcfscoupon.domain.coupon.support;

import com.coupop.fcfscoupon.domain.coupon.model.RandomCodeGenerator;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RandomCodeGeneratorImpl implements RandomCodeGenerator {

    private final String secretKey;

    public RandomCodeGeneratorImpl(@Value("${coupon.code.secretKey}") final String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String generate(final Long sequence) {
        final long timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        return Integer.toHexString(Objects.hash(timestamp, sequence, secretKey));
    }
}
