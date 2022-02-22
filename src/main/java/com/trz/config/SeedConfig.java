package com.trz.config;

import com.trz.config.seed.Seed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SeedConfig {
    public final Seed seed;

    public SeedConfig(Seed seed) {
        this.seed = seed;
    }

    @Bean
    public void seedItem() {
        seed.seedItem();
    }

    @Profile("dev")
    @Bean
    public void seedSurvivor() {
        seed.seedSurvivor();
    }
}
