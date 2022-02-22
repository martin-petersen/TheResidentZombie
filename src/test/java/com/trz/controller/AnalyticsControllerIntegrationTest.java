package com.trz.controller;

import com.trz.config.seed.Seed;
import com.trz.repository.SurvivorRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AnalyticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private Seed seed;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
        seed.seedItem();
        seed.seedSurvivor();
    }

    @Test
    void should_Report_Survivor_As_Infected() throws Exception {
        mockMvc.perform(get("/analytics")).andExpect(status().isOk());
    }
}