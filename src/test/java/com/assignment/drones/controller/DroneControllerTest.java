package com.assignment.drones.controller;

import com.assignment.drones.exception.RuntimeValidationException;
import com.assignment.drones.model.domain.Model;
import com.assignment.drones.model.domain.State;
import com.assignment.drones.model.dto.DroneDTO;
import com.assignment.drones.model.dto.MedicationDTO;
import com.assignment.drones.service.DroneService;
import com.assignment.drones.service.ImageService;
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

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class DroneControllerTest {
    @MockBean
    private DroneService droneService;
    @MockBean
    private ImageService imageService;
    @Autowired
    private DroneController droneController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createDrone_shouldReturnCreatedDroneData_withCreatedStatusCode() throws Exception {
        String drone = "{\"serialNumber\": \"iwoue1308wewe38274eg\", \"model\" : \"Heavyweight\", \"weightLimit\" : 490.22, \"batteryCapacity\" : 42.44}";

        DroneDTO dto = new DroneDTO();
        dto.setSerialNumber("iwoue1308wewe38274eg");
        dto.setBatteryCapacity(BigDecimal.valueOf(42.44));
        dto.setState(State.IDLE);
        dto.setModel(Model.Heavyweight);
        dto.setWeightLimit(BigDecimal.valueOf(490.22));

        when(droneService.createDrone(any())).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                        .content(drone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.serialNumber", is("iwoue1308wewe38274eg")))
                .andExpect(jsonPath("$.state", is("IDLE")));
    }

    @Test
    public void createDrone_shouldReturnErrorMessage_whenValidationsErrorsExist() throws Exception {
        String user = "{\"serialNumber\": \"iwoue1308wjihwe873849euyru837248iueyriu83728472uewyr8273894iuewyr8274yewury237498ieuyruiweyr892374uyrewiuyr892734ewe38274eg\", " +
                "\"model\" : \"Heavyweight\", \"weightLimit\" : 580.22, \"batteryCapacity\" : 42.44}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("serialNumber=Serial number should not be greater than 100 characters; weightLimit=Weight limit should not be greater than 500")));
    }

    @Test
    public void loadMedications_shouldReturnAllMedicationData_with200OkStatusCode() throws Exception {
        String medications = "{\"medications\": [{\"code\": \"32498758\", \"name\": \"RAPISOL\", \"weight\": 28.88}, { \"code\": \"21564938\", \"name\": \"ZINCOVIT\", \"weight\": 84.12}]}";

        List<MedicationDTO> medicationDTOS = new ArrayList<>();

        MedicationDTO dto1 = new MedicationDTO();
        dto1.setId(1);
        dto1.setName("PARACETAMOL");
        dto1.setCode("121221");
        dto1.setWeight(BigDecimal.TEN);

        MedicationDTO dto2 = new MedicationDTO();
        dto2.setId(2);
        dto2.setName("RAPISOL");
        dto2.setCode("32498758");
        dto2.setWeight(BigDecimal.valueOf(28.88));

        MedicationDTO dto3 = new MedicationDTO();
        dto3.setId(3);
        dto3.setName("ZINCOVIT");
        dto3.setCode("21564938");
        dto3.setWeight(BigDecimal.valueOf(84.12));

        medicationDTOS.add(dto1);
        medicationDTOS.add(dto2);
        medicationDTOS.add(dto3);

        when(droneService.addMedications(anyString(), any())).thenReturn(medicationDTOS);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones/iwoue1308wewe38274eg/medications")
                        .header("X-Syy-Correlation-Id", "sjakd-sadhk-iwquei-wqiuei")
                        .content(medications)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.medications.length()", is(3)))
                .andExpect(header().string("X-Syy-Correlation-Id", is("sjakd-sadhk-iwquei-wqiuei")));
    }

    @Test
    public void loadMedications_shouldReturnErrorMessage_whenDroneDoesNotExist() throws Exception {
        String medications = "{\"medications\": [{\"code\": \"32498758\", \"name\": \"RAPISOL\", \"weight\": 28.88}, { \"code\": \"21564938\", \"name\": \"ZINCOVIT\", \"weight\": 84.12}]}";

        when(droneService.addMedications(anyString(), any())).thenThrow(new EntityNotFoundException("No drone exists for the given serial number: iwoue1308wewe38274eg"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones/iwoue1308wewe38274eg/medications")
                        .header("X-Syy-Correlation-Id", "sjakd-sadhk-iwquei-wqiuei")
                        .content(medications)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("No drone exists for the given serial number: iwoue1308wewe38274eg")))
                .andExpect(header().string("X-Syy-Correlation-Id", is("sjakd-sadhk-iwquei-wqiuei")));
    }

    @Test
    public void loadMedications_shouldReturnErrorMessage_whenDroneWeightLimitExceed() throws Exception {
        String medications = "{\"medications\": [{\"code\": \"32498758\", \"name\": \"RAPISOL\", \"weight\": 28.88}, { \"code\": \"21564938\", \"name\": \"ZINCOVIT\", \"weight\": 84.12}]}";

        when(droneService.addMedications(anyString(), any())).thenThrow(new RuntimeValidationException("Medications loading failed since drone weight limit exceeded"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drones/iwoue1308wewe38274eg/medications")
                        .header("X-Syy-Correlation-Id", "sjakd-sadhk-iwquei-wqiuei")
                        .content(medications)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is("failure")))
                .andExpect(jsonPath("$.message", is("Medications loading failed since drone weight limit exceeded")))
                .andExpect(header().string("X-Syy-Correlation-Id", is("sjakd-sadhk-iwquei-wqiuei")));
    }

}
