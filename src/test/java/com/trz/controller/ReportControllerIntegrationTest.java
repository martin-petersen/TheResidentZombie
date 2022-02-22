package com.trz.controller;

import com.trz.config.seed.Seed;
import com.trz.model.Survivor;
import com.trz.repository.SurvivorRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private Seed seed;

    @Autowired
    private SurvivorRepository survivorRepository;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
        seed.seedItem();
        seed.seedSurvivor();
    }

    @Test
    void should_Report_Survivor_As_Infected() throws Exception {
        List<Survivor> survivors = survivorRepository.findAll();
        Survivor survivor1 = survivors.get(0);
        Survivor survivor2 = survivors.get(1);
        mockMvc.perform(post("/reports/" + survivor1.getId() + "/denunciation/" + survivor2.getId())).andExpect(status().isCreated());
    }
}