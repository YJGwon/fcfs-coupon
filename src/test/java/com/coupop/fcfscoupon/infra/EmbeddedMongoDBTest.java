package com.coupop.fcfscoupon.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class EmbeddedMongoDBTest {

    @DisplayName("Embedded MongoDB에 연결된 MongoTemplate bean을 사용할 수 있다.")
    @Test
    void integrate(@Autowired final MongoTemplate mongoTemplate) {
        assertThat(mongoTemplate.getDb()).isNotNull();
    }
}
