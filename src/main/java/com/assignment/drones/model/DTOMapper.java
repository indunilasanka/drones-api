package com.assignment.drones.model;

import com.assignment.drones.model.domain.Drone;
import com.assignment.drones.model.domain.Medication;
import com.assignment.drones.model.domain.State;
import com.assignment.drones.model.dto.DroneDTO;
import com.assignment.drones.model.dto.MedicationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide mapping functionalities map DTOs and domain models
 */
public class DTOMapper {
    public static Drone mapToDrone(DroneDTO dto) {
        Drone drone = new Drone();
        drone.setSerialNumber(dto.getSerialNumber());
        drone.setModel(dto.getModel());
        drone.setBatteryCapacity(dto.getBatteryCapacity());
        drone.setWeightLimit(dto.getWeightLimit());
        drone.setState(State.IDLE);
        return drone;
    }

    public static DroneDTO mapToDroneDTO(Drone drone) {
        DroneDTO dto = new DroneDTO();
        dto.setSerialNumber(drone.getSerialNumber());
        dto.setModel(drone.getModel());
        dto.setWeightLimit(drone.getWeightLimit());
        dto.setBatteryCapacity(drone.getBatteryCapacity());
        dto.setState(drone.getState());
        return dto;
    }

    public static MedicationDTO mapToMedicationDTO(Medication medication) {
        MedicationDTO dto = new MedicationDTO();
        dto.setId(medication.getId());
        dto.setCode(medication.getCode());
        dto.setName(medication.getName());
        dto.setWeight(medication.getWeight());
        return dto;
    }

    public static Medication mapToMedication(MedicationDTO dto) {
        Medication medication = new Medication();
        medication.setName(dto.getName());
        medication.setCode(dto.getCode());
        medication.setWeight(dto.getWeight());
        medication.setImage(dto.getImage());
        return medication;
    }

    public static List<MedicationDTO> mapMedicationListToDTOs(List<Medication> medications) {
        List<MedicationDTO> medicationDTOs = new ArrayList<>();

        for (Medication medication : medications) {
            medicationDTOs.add(mapToMedicationDTO(medication));
        }
        return medicationDTOs;
    }

    public static List<DroneDTO> mapDroneListToDTOs(List<Drone> drones) {
        List<DroneDTO> droneDTOs = new ArrayList<>();

        for (Drone drone : drones) {
            droneDTOs.add(mapToDroneDTO(drone));
        }
        return droneDTOs;
    }

}
