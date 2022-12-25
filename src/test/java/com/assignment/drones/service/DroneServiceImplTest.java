package com.assignment.drones.service;

import com.assignment.drones.exception.RuntimeValidationException;
import com.assignment.drones.model.domain.Drone;
import com.assignment.drones.model.domain.Medication;
import com.assignment.drones.model.domain.Model;
import com.assignment.drones.model.domain.State;
import com.assignment.drones.model.dto.MedicationDTO;
import com.assignment.drones.repository.DroneRepository;
import com.assignment.drones.repository.MedicationRepository;
import com.assignment.drones.service.impl.DroneServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class DroneServiceImplTest {
    private DroneRepository droneRepository;
    private MedicationRepository medicationRepository;
    private DroneServiceImpl droneService;

    @BeforeEach
    public void setup() {
        droneRepository = mock(DroneRepository.class);
        medicationRepository = mock(MedicationRepository.class);
        droneService = new DroneServiceImpl(droneRepository, medicationRepository);
    }

    @Test
    public void addMedications_shouldThrowException_whenEntityDoesNotExist() {
        MedicationDTO medicationDTO1 = new MedicationDTO();
        medicationDTO1.setName("RAPISOL");
        medicationDTO1.setCode("32498758");
        medicationDTO1.setWeight(BigDecimal.valueOf(28.88));

        Drone drone = new Drone();
        drone.setSerialNumber("iwoue1308wewe38274eg");
        drone.setBatteryCapacity(BigDecimal.valueOf(42.44));
        drone.setState(State.IDLE);
        drone.setModel(Model.Heavyweight);
        drone.setWeightLimit(BigDecimal.valueOf(490.22));

        when(droneRepository.findById("iwoue1308wewe38274eg")).thenThrow(new EntityNotFoundException("No drone for given serial number"));
        Exception exception = Assert.assertThrows(EntityNotFoundException.class, () -> {
            droneService.addMedications("iwoue1308wewe38274eg", List.of(medicationDTO1));
        });
        Assert.assertEquals("No drone for given serial number", exception.getMessage());
    }

    @Test
    public void addMedications_shouldThrowException_whenBatteryLevelIsBelowTheThreshold() {
        MedicationDTO medicationDTO1 = new MedicationDTO();
        medicationDTO1.setName("RAPISOL");
        medicationDTO1.setCode("32498758");
        medicationDTO1.setWeight(BigDecimal.valueOf(28.88));

        Drone drone = new Drone();
        drone.setSerialNumber("iwoue1308wewe38274eg");
        drone.setBatteryCapacity(BigDecimal.valueOf(22.44));
        drone.setState(State.IDLE);
        drone.setModel(Model.Heavyweight);
        drone.setWeightLimit(BigDecimal.valueOf(490.22));

        when(droneRepository.findById("iwoue1308wewe38274eg")).thenReturn(Optional.of(drone));
        Exception exception = Assert.assertThrows(RuntimeValidationException.class, () -> {
            droneService.addMedications("iwoue1308wewe38274eg", List.of(medicationDTO1));
        });
        Assert.assertEquals("Drone battery level is below the required margin to load medications", exception.getMessage());
    }

    @Test
    public void addMedications_shouldThrowException_whenDroneWeightLimitExceeded() {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("PARACETAMOL");
        medication.setCode("121221");
        medication.setWeight(BigDecimal.TEN);

        Medication medication1 = new Medication();
        medication1.setId(2);
        medication1.setName("RAPISOL");
        medication1.setCode("32498758");
        medication1.setWeight(BigDecimal.valueOf(28.88));

        Medication medication2 = new Medication();
        medication2.setId(3);
        medication2.setName("ZINCOVIT");
        medication2.setCode("21564938");
        medication2.setWeight(BigDecimal.valueOf(84.12));

        Drone drone = new Drone();
        drone.setSerialNumber("iwoue1308wewe38274eg");
        drone.setBatteryCapacity(BigDecimal.valueOf(42.44));
        drone.setState(State.IDLE);
        drone.setModel(Model.Heavyweight);
        drone.setWeightLimit(BigDecimal.valueOf(100.00));
        drone.setMedications(new ArrayList<>(List.of(medication)));

        Drone drone1 = new Drone();
        drone1.setSerialNumber("iwoue1308wewe38274eg");
        drone1.setBatteryCapacity(BigDecimal.valueOf(42.44));
        drone1.setState(State.IDLE);
        drone1.setModel(Model.Heavyweight);
        drone1.setWeightLimit(BigDecimal.valueOf(100.00));
        drone1.setMedications(List.of(medication, medication1, medication2));

        MedicationDTO medicationDTO1 = new MedicationDTO();
        medicationDTO1.setName("RAPISOL");
        medicationDTO1.setCode("32498758");
        medicationDTO1.setWeight(BigDecimal.valueOf(28.88));

        MedicationDTO medicationDTO2 = new MedicationDTO();
        medicationDTO2.setName("ZINCOVIT");
        medicationDTO2.setCode("21564938");
        medicationDTO2.setWeight(BigDecimal.valueOf(84.12));

        when(droneRepository.findById(any())).thenReturn(Optional.of(drone));
        when(medicationRepository.save(any())).thenReturn(medication1).thenReturn(medication2);
        when(droneRepository.save(any())).thenReturn(drone1);

        Exception exception = Assert.assertThrows(RuntimeValidationException.class, () -> {
            droneService.addMedications("iwoue1308wewe38274eg", List.of(medicationDTO1, medicationDTO2));
        });
        Assert.assertEquals("Medications loading failed since drone weight limit exceeded", exception.getMessage());
    }

    @Test
    public void addMedications_shouldReturnAllMedications_whenMedicationsLoadingSuccess() {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("PARACETAMOL");
        medication.setCode("121221");
        medication.setWeight(BigDecimal.TEN);

        Medication medication1 = new Medication();
        medication1.setId(2);
        medication1.setName("RAPISOL");
        medication1.setCode("32498758");
        medication1.setWeight(BigDecimal.valueOf(28.88));

        Medication medication2 = new Medication();
        medication2.setId(3);
        medication2.setName("ZINCOVIT");
        medication2.setCode("21564938");
        medication2.setWeight(BigDecimal.valueOf(84.12));

        Drone drone = new Drone();
        drone.setSerialNumber("iwoue1308wewe38274eg");
        drone.setBatteryCapacity(BigDecimal.valueOf(42.44));
        drone.setState(State.IDLE);
        drone.setModel(Model.Heavyweight);
        drone.setWeightLimit(BigDecimal.valueOf(490.22));
        drone.setMedications(new ArrayList<>(List.of(medication)));

        Drone drone1 = new Drone();
        drone1.setSerialNumber("iwoue1308wewe38274eg");
        drone1.setBatteryCapacity(BigDecimal.valueOf(42.44));
        drone1.setState(State.IDLE);
        drone1.setModel(Model.Heavyweight);
        drone1.setWeightLimit(BigDecimal.valueOf(490.22));
        drone1.setMedications(List.of(medication, medication1, medication2));

        MedicationDTO medicationDTO1 = new MedicationDTO();
        medicationDTO1.setName("RAPISOL");
        medicationDTO1.setCode("32498758");
        medicationDTO1.setWeight(BigDecimal.valueOf(28.88));

        MedicationDTO medicationDTO2 = new MedicationDTO();
        medicationDTO2.setName("ZINCOVIT");
        medicationDTO2.setCode("21564938");
        medicationDTO2.setWeight(BigDecimal.valueOf(84.12));

        when(droneRepository.findById(any())).thenReturn(Optional.of(drone));
        when(medicationRepository.save(any())).thenReturn(medication1).thenReturn(medication2);
        when(droneRepository.save(any())).thenReturn(drone1);

        List<MedicationDTO> result = droneService.addMedications("iwoue1308wewe38274eg", List.of(medicationDTO1, medicationDTO2));
        verify(medicationRepository, times(2)).save(any());
        verify(droneRepository, times(2)).save(any());
        Assert.assertEquals(3, result.size());
    }


}
