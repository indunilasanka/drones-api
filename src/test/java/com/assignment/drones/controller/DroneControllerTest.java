package com.assignment.drones.controller;

import com.assignment.drones.model.dto.DroneDTO;
import com.assignment.drones.model.domain.Model;
import com.assignment.drones.model.domain.State;
import com.assignment.drones.service.DroneService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class DroneControllerTest {
    @MockBean
    private DroneService droneService;
    @Autowired
    private DroneController droneController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createDrone_shouldReturnCreatedDoneData_withCreatedStatusCode() throws Exception {
        String user = "{\"serialNumber\": \"iwoue1308wewe38274eg\", \"model\" : \"Heavyweight\", \"weightLimit\" : 490.22, \"batteryCapacity\" : 42.44}";

        DroneDTO dto = new DroneDTO();
        dto.setSerialNumber("iwoue1308wewe38274eg");
        dto.setBatteryCapacity(BigDecimal.valueOf(42.44));
        dto.setState(State.IDLE);
        dto.setModel(Model.Heavyweight);
        dto.setWeightLimit(BigDecimal.valueOf(490.22));

        when(droneService.createDrone(any())).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.serialNumber", is("iwoue1308wewe38274eg")))
                .andExpect(jsonPath("$.state", is("IDLE")));
    }

    @Test
    public void createDrone_shouldReturnErrorMessage_withBadRequestStatusCode() throws Exception {
        String user = "{\"serialNumber\": \"iwoue1308wjihwe873849euyru837248iueyriu83728472uewyr8273894iuewyr8274yewury237498ieuyruiweyr892374uyrewiuyr892734ewe38274eg\", " +
                "\"model\" : \"Heavyweight\", \"weightLimit\" : 580.22, \"batteryCapacity\" : 42.44}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("serialNumber=Serial number should not be greater than 100 characters; weightLimit=Weight limit should not be greater than 500")));
    }

}
