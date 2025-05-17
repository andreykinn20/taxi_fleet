package com.andreine.taxifleet.config;

import java.util.Map;

import com.andreine.taxifleet.container.SingletonEmbeddedKafkaBroker;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@TestConfiguration
public class KafkaConsumerConfig {

    @Bean(destroyMethod = "close")
    public Consumer<String, String> testKafkaConsumer() {
        var embeddedKafka = SingletonEmbeddedKafkaBroker.getInstance();

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(consumerProps);
    }

}
