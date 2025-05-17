package com.andreine.taxifleet;

import com.andreine.taxifleet.container.SingletonEmbeddedKafkaBroker;
import com.andreine.taxifleet.container.SingletonPostgresContainer;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = {
        BingoEventRecompletionApplication.class
    }
)
@Testcontainers
@EmbeddedKafka(partitions = 1, topics = {"taxi_fleet_booking_created"})
public class BaseFunctionalTest {

    private static final EmbeddedKafkaBroker embeddedKafka = SingletonEmbeddedKafkaBroker.getInstance();

    private static final SingletonPostgresContainer postgres = SingletonPostgresContainer.getInstance();

    @DynamicPropertySource
    static void register(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.kafka.bootstrap-servers", embeddedKafka::getBrokersAsString);
    }

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected TaxiRepository taxiRepository;

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        taxiRepository.deleteAll();
    }

}
