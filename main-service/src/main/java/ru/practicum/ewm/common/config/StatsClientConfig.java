package ru.practicum.ewm.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.explore_with_me.StatsClient;

@Configuration
public class StatsClientConfig {

    @Bean
    StatsClient statsClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatsClient(builder.build());
    }
}