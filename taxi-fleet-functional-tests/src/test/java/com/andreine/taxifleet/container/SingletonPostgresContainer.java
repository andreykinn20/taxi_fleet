package com.andreine.taxifleet.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class SingletonPostgresContainer extends PostgreSQLContainer<SingletonPostgresContainer> {

    private static SingletonPostgresContainer instance;

    private SingletonPostgresContainer() {
        super("postgres:15");
        withDatabaseName("testdb");
        withUsername("test");
        withPassword("test");
    }

    public static  SingletonPostgresContainer getInstance() {
        if (instance == null) {
            instance = new SingletonPostgresContainer();
            instance.start();
        }
        return instance;
    }

}