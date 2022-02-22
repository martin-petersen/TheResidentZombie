package com.trz.controller;

import com.trz.config.seed.Seed;
import com.trz.model.Survivor;
import com.trz.service.SurvivorService;
import com.trz.service.TradeService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SurvivorControllerIntegrationTest {

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
    void should_Create_Survivor() throws Exception {
        mockMvc.perform(post("/survivors").contentType("application/json").content(
                "{" +
                        "  \"name\":\"Martin\"," +
                        "  \"age\":24," +
                        "  \"gender\":\"Male\"," +
                        "  \"items\":[" +
                        "    {" +
                        "      \"item\":\"AK47\"," +
                        "      \"amount\":1" +
                        "    }," +
                        "    {" +
                        "      \"item\":\"Fuji Water\"," +
                        "      \"amount\":1" +
                        "    }," +
                        "    {" +
                        "      \"item\":\"Campbell Soup\"," +
                        "      \"amount\":1" +
                        "    }," +
                        "    {" +
                        "      \"item\":\"First Aid Pouch\"," +
                        "      \"amount\":1" +
                        "    }" +
                        "  ]," +
                        "  \"latitude\":-78.5457," +
                        "  \"longitude\":-45.6244" +
                        "}"
        )).andExpect(status().isCreated());
    }

    @Test
    void should_Update_Survivor_Location() throws Exception {
        mockMvc.perform(put("/survivors/ffcb1e19-c9cf-42a3-a0b9-47f105985d23/location").contentType("application/json").content(
                "{" +
                        "  \"latitude\":-55.45456," +
                        "  \"longitude\":54.44145" +
                        "}"
        )).andExpect(status().isOk());
    }

    @Test
    void should_Trade_Successfully() throws Exception {
        mockMvc.perform(post("/survivors/ffcb1e19-c9cf-42a3-a0b9-47f105985d23/trade/2a0197bf-2010-4cd9-ab64-195a3a8869bf").contentType("application/json").content(
                "{" +
                        "  \"supply\":[" +
                        "    {" +
                        "      \"item\":\"First Aid Pouch\"," +
                        "      \"amount\":4" +
                        "    }" +
                        "  ]," +
                        "  \"demand\":[" +
                        "    {" +
                        "      \"item\":\"AK47\"," +
                        "      \"amount\":5" +
                        "    }" +
                        "  ]" +
                        "}"
        )).andExpect(status().isOk());
    }
}