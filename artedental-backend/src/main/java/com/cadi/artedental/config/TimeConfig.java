package com.cadi.artedental.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    public static final ZoneId APP_ZONE =
        ZoneId.of("America/Mexico_City");

    @Bean
    public Clock applicationClock() {
        return Clock.system(APP_ZONE);
    }
}