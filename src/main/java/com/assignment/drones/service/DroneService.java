package com.assignment.drones.service;

import com.assignment.drones.dto.BatteryCapacityDTO;
import com.assignment.drones.dto.DroneDTO;
import com.assignment.drones.dto.MedicationDTO;

import java.util.List;

public interface DroneService {
    DroneDTO createDrone(DroneDTO dto);

    List<MedicationDTO> addMedications(String serialNumber, List<MedicationDTO> medicationList);

    List<MedicationDTO> getLoadedMedications(String serialNumber);

    BatteryCapacityDTO getBatteryLevel(String serialNumber);

    List<DroneDTO> getDronesForGivenState(String state);

    List<DroneDTO> getAllDrones();
}
