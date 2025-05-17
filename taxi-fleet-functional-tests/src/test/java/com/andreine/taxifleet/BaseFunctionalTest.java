package com.andreine.taxifleet;

import com.andreine.taxifleet.config.KafkaConsumerConfig;
import com.andreine.taxifleet.container.SingletonEmbeddedKafkaBroker;
import com.andreine.taxifleet.container.SingletonPostgresContainer;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import com.andreine.taxifleet.steps.ConsumerTopicSteps;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = {
        BingoEventRecompletionApplication.class,
        KafkaConsumerConfig.class
    }
)
@ActiveProfiles("test")
@Testcontainers
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

    static {
        SingletonEmbeddedKafkaBroker.getInstance().addTopics("taxi_fleet_booking_created");
    }

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected TaxiRepository taxiRepository;

    @Autowired
    protected ConsumerTopicSteps consumerTopicSteps;

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        taxiRepository.deleteAll();
    }

}
