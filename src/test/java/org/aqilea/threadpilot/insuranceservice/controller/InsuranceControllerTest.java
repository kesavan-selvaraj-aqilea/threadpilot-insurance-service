package org.aqilea.threadpilot.insuranceservice.controller;

import org.aqilea.threadpilot.insuranceservice.model.Insurance;
import org.aqilea.threadpilot.insuranceservice.service.InsuranceService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceService insuranceService;

    @Test
    void shouldReturnInsurancesForPerson() throws Exception {
        Insurance insurance = new Insurance();
        insurance.setType("Car insurance");
        Mockito.when(insuranceService.getPersonInsurances(anyString()))
                .thenReturn(List.of(insurance));

        mockMvc.perform(get("/insurances/19900101-1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Car insurance"));
    }

    @Test
    void shouldReturnEmptyListWhenNoInsurances() throws Exception {
        Mockito.when(insuranceService.getPersonInsurances(anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/insurances/19900101-1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // TODO: Enable this test when authentication is implemented
    @Disabled("Authentication not implemented yet")
    void shouldReturnUnauthorizedWhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/insurances/19900101-1234")
                        .header("Authorization", "InvalidToken")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnMultipleInsurancesForPerson() throws Exception {
        Insurance carInsurance = new Insurance();
        carInsurance.setType("Car insurance");

        Insurance homeInsurance = new Insurance();
        homeInsurance.setType("Home insurance");

        Mockito.when(insuranceService.getPersonInsurances(anyString()))
                .thenReturn(List.of(carInsurance, homeInsurance));

        mockMvc.perform(get("/insurances/19900101-1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Car insurance"))
                .andExpect(jsonPath("$[1].type").value("Home insurance"));
    }
}
