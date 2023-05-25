package com.coupop.fcfscoupon.mq.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@Configuration
public class EmbeddedKafkaConfig {

    @Bean
    EmbeddedKafkaBroker broker() {
        return new EmbeddedKafkaBroker(1).kafkaPorts(9092);
    }

    @Bean
    NewTopic topic() {
        return TopicBuilder.name("issueCoupon")
                .build();
    }
}
