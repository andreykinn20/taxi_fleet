package com.andreine.taxifleet.config;

import com.andreine.taxifleet.integration.kafka.producer.BookingMessageProducer;
import com.andreine.taxifleet.integration.kafka.producer.model.BookingMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Taxi fleet Kafka auto configuration.
 */
@AutoConfiguration
public class TaxiFleetKafkaAutoConfiguration {

    @Bean
    public BookingMessageProducer bookingMessageProducer(@Value("${kafka.publisher.booking-created.topic-name}") String topicName,
                                                         KafkaTemplate<String, BookingMessageDto> kafkaTemplate) {
        return new BookingMessageProducer(topicName, kafkaTemplate);
    }

}
