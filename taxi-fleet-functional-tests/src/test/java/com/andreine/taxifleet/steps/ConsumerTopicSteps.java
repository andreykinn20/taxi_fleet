package com.andreine.taxifleet.steps;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.andreine.taxifleet.integration.kafka.producer.model.BookingMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class ConsumerTopicSteps {

    @Value("${kafka.subscriber.booking-created.topic-name}")
    protected String bookingCreatedTopic;

    private static final long POLL_TIMEOUT_MS = 1000L;

    private final Consumer<String, String> consumer;

    private final ObjectMapper objectMapper;

    public BookingMessageDto getSingleBookingMessage() {
        List<BookingMessageDto> bookingMessages = poll(bookingCreatedTopic, 10, ChronoUnit.SECONDS, 1)
            .stream()
            .map(ConsumerRecord::value)
            .map(event -> deserializeValue(event, BookingMessageDto.class))
            .toList();

        assertThat(bookingMessages).hasSize(1);

        return bookingMessages.getFirst();
    }

    private List<ConsumerRecord<String, String>> poll(String topic, long timeout, TemporalUnit timeUnit, int expectedMessages) {
        List<ConsumerRecord<String, String>> records = new ArrayList<>();
        Awaitility.await().timeout(Duration.of(timeout, timeUnit)).untilAsserted(() -> {
            List<ConsumerRecord<String, String>> receivedRecords = this.pollForRecords(Duration.ofMillis(POLL_TIMEOUT_MS), topic);
            records.addAll(receivedRecords);
            Assertions.assertThat(records.size()).isGreaterThanOrEqualTo(expectedMessages);
        });
        return records;
    }

    private List<ConsumerRecord<String, String>> pollForRecords(Duration timeout, String... topic) {
        Set<String> topics = Arrays.stream(topic).collect(Collectors.toSet());
        this.consumer.subscribe(topics);
        ConsumerRecords<String, String> received = this.consumer.poll(timeout);
        if (received.isEmpty()) {
            return Collections.emptyList();
        } else {
            Objects.requireNonNull(received);
            return Arrays.stream(topic)
                .map(received::records)
                .flatMap(ConsumerTopicSteps::getRecordStream)
                .toList();
        }
    }

    private static Stream<ConsumerRecord<String, String>> getRecordStream(Iterable<ConsumerRecord<String, String>> t) {
        return StreamSupport.stream(t.spliterator(), false);
    }

    @SneakyThrows
    private <T> T deserializeValue(String value, Class<T> valueType) {
        return objectMapper.readValue(value, valueType);
    }

}
