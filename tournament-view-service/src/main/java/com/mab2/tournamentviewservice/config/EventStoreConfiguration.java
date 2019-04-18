package com.mab2.tournamentviewservice.config;

import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.EventStoreBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfiguration {

    @Value("${eventstore.hostname:127.0.0.1}")
    private String hostname;

    @Value("${eventstore.port:1113}")
    private Integer port;

    @Value("${eventstore.login:admin}")
    private String login;

    @Value("${eventstore.password:changeit}")
    private String password;

    @Bean
    public EventStore eventStore() {
        return EventStoreBuilder.newBuilder()
                .singleNodeAddress(hostname, port)
                .userCredentials(login, password)
                .build();
    }

}