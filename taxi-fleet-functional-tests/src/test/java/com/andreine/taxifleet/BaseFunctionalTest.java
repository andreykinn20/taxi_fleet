package com.andreine.taxifleet;

import com.andreine.taxifleet.config.SingletonPostgresContainer;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = {
        BingoEventRecompletionApplication.class
    }
)
@ActiveProfiles("test")
@Testcontainers
public class BaseFunctionalTest {

    @DynamicPropertySource
    static void register(DynamicPropertyRegistry registry) {
        SingletonPostgresContainer postgres = SingletonPostgresContainer.getInstance();
        postgres.start();

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected TaxiRepository taxiRepository;

}
