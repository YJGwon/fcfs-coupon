package com.coupop.fcfscoupon.mq.config;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(EmbeddedKafkaConfig.class)
class EmbeddedKafkaConfigTest {

    @DisplayName("Embedded Kafka에 연결할 수 있다.")
    @Test
    void connectToEmbeddedKafka() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final DefaultKafkaProducerFactory<Integer, String> producerFactory
                = new DefaultKafkaProducerFactory<>(props);

        final KafkaTemplate<Integer, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        assertThatNoException()
                .isThrownBy(() -> kafkaTemplate.send("issueCoupon", "fakeData"));
    }
}
