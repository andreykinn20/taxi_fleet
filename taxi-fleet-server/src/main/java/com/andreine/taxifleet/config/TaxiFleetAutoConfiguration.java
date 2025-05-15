package com.andreine.taxifleet.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Taxi fleet auto configuration.
 */
@AutoConfiguration
@EnableRetry
@EnableJpaRepositories(basePackages = "com.andreine.taxifleet.persistence.repository")
public class TaxiFleetAutoConfiguration {

}