package com.coupop.fcfscoupon.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class RandomCodeGenerator {

    public static String generate(final Long sequence, final String secretKey) {
        final long timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        return Integer.toHexString(Objects.hash(timestamp, sequence, secretKey));
    }
}
