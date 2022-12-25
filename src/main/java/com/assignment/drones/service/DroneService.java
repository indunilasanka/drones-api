package com.assignment.drones.service;

import com.assignment.drones.model.dto.BatteryCapacityDTO;
import com.assignment.drones.model.dto.DroneDTO;
import com.assignment.drones.model.dto.MedicationDTO;

import java.util.List;

/**
 * Define business operations for drone API
 */
public interface DroneService {

    /**
     * Create new drone
     *
     * @return Created new drone
     */
    DroneDTO createDrone(DroneDTO dto);

    /**
     * Load medications into the existing drone
     *
     * @return List of medications
     */
    List<MedicationDTO> addMedications(String serialNumber, List<MedicationDTO> medicationList);

    /**
     * List down loaded medications in the given drone
     *
     * @return list of medications
     */
    List<MedicationDTO> getLoadedMedications(String serialNumber);

    /**
     * Get battery capacity of the given drone
     *
     * @return Battery capacity
     */
    BatteryCapacityDTO getBatteryLevel(String serialNumber);

    /**
     * Get all drone information of the given state
     *
     * @return List of drones
     */
    List<DroneDTO> getDronesForGivenState(String state);

    /**
     * List down all drones
     *
     * @return List of drones
     */
    List<DroneDTO> getAllDrones();
}
