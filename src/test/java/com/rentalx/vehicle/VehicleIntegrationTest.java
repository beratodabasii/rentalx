package com.rentalx.vehicle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void shouldGetVehicleById() throws Exception {
        mockMvc.perform(get("/api/v1/vehicles/4"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void shouldReturn404WhenVehicleNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/vehicles/99999"))
                .andExpect(status().isNotFound());
    }
}
