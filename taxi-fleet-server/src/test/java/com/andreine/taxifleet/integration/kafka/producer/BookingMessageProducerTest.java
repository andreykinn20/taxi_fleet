package com.andreine.taxifleet.integration.kafka.producer;

import com.andreine.taxifleet.integration.kafka.producer.model.BookingMessageDto;
import com.andreine.taxifleet.service.model.Booking;
import com.andreine.taxifleet.service.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingMessageProducerTest {

    private static final String TOPIC_NAME = "booking-created";

    @Mock
    private KafkaTemplate<String, BookingMessageDto> kafkaTemplate;

    private BookingMessageProducer bookingMessageProducer;

    @BeforeEach
    void setUp() {
        bookingMessageProducer = new BookingMessageProducer(TOPIC_NAME, kafkaTemplate);
    }

    @Test
    void shouldPublishBookingMessage() {
        var booking = Booking.builder()
            .id(1L)
            .userId(2L)
            .taxiId(3L)
            .createdOnTs(4L)
            .fromLocation(new Location(10.0, 20.0))
            .toLocation(new Location(30.0, 40.0))
            .createdOnTs(100500L)
            .build();
        var bookingMessageDto = BookingMessageDto.builder()
            .bookingId(1L)
            .userId(2L)
            .taxiId(3L)
            .createdOnTs(100500L)
            .fromLocation(new Location(10.0, 20.0))
            .toLocation(new Location(30.0, 40.0))
            .build();

        bookingMessageProducer.publishBookingMessage(booking, 3L);

        verify(kafkaTemplate).send(TOPIC_NAME, bookingMessageDto);
    }

}
