package com.coupop.fcfscoupon.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class EmbeddedMongoDBTest {
    @Test
    void example(@Autowired final MongoTemplate mongoTemplate) {
        assertThat(mongoTemplate.getDb()).isNotNull();
    }
}
