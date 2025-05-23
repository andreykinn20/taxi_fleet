package com.andreine.taxifleet.container;

import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;

public class SingletonEmbeddedKafkaBroker {

    private static EmbeddedKafkaBroker instance;

    private SingletonEmbeddedKafkaBroker() {
    }

    public static EmbeddedKafkaBroker getInstance() {
        if (instance == null) {
            instance = new EmbeddedKafkaKraftBroker(1, 1);
            instance.afterPropertiesSet();
        }
        return instance;
    }

}