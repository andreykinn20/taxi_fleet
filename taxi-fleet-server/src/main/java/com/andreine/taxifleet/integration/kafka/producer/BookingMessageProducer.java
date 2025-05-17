package com.andreine.taxifleet.integration.kafka.producer;

import com.andreine.taxifleet.integration.kafka.producer.model.BookingMessageDto;
import com.andreine.taxifleet.service.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Booking message producer.
 */
@RequiredArgsConstructor
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
        var message = BookingMessageDto.builder()
            .bookingId(booking.id())
            .userId(booking.userId())
            .taxiId(taxiId)
            .createdOnTs(booking.createdOnTs())
            .fromLocation(booking.fromLocation())
            .toLocation(booking.toLocation())
            .build();

        kafkaTemplate.send(topicName, message);
    }

}