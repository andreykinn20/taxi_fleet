package com.andreine.taxifleet.integration.kafka.producer;

import com.andreine.taxifleet.integration.kafka.producer.model.BookingMessageDto;
import com.andreine.taxifleet.service.model.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Booking message producer.
 */
@RequiredArgsConstructor
@Slf4j
public class BookingMessageProducer {

    private final String topicName;

    private final KafkaTemplate<String, BookingMessageDto> kafkaTemplate;

    /**
     * Sends booking message to taxi.
     *
     * @param booking booking
     * @param taxiId  taxi id
     */
    public void publishBookingMessage(Booking booking, Long taxiId) {
        BookingMessageDto message = BookingMessageDto.builder()
            .taxiId(taxiId)
            .bookingId(booking.id())
            .userId(booking.userId())
            .createdOnTs(booking.createdOnTs())
            .fromLocation(booking.fromLocation())
            .toLocation(booking.toLocation())
            .build();

        kafkaTemplate.send(topicName, message);

        log.info("Taxi {} was notified about booking {}", taxiId, booking.id());
    }

}