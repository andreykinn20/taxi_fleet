package com.andreine.taxifleet.config;

import java.util.Map;

import com.andreine.taxifleet.container.SingletonEmbeddedKafkaBroker;
import com.andreine.taxifleet.steps.ConsumerTopicSteps;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@TestConfiguration
public class KafkaConsumerConfig {

    @Bean(destroyMethod = "close")
    public Consumer<String, String> testKafkaConsumer() {
        EmbeddedKafkaBroker embeddedKafka = SingletonEmbeddedKafkaBroker.getInstance();
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

        return cf.createConsumer();
    }

    @Bean
    public ConsumerTopicSteps consumerTopicSteps(Consumer<String, String> consumer, ObjectMapper objectMapper) {
        return new ConsumerTopicSteps(consumer, objectMapper);
    }

}
