package com.assignment.drones.controller;

import com.assignment.drones.model.dto.*;
import com.assignment.drones.service.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Drone API interfaces
 */
@RestController
@RequestMapping("/api/drones")
public class DroneController extends BaseController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping
    public ResponseEntity<DroneListDTO> getAllDrones() {
        List<DroneDTO> drones = droneService.getAllDrones();

        DroneListDTO responseDTO = new DroneListDTO();
        responseDTO.setDrones(drones);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<DroneDTO> createDrone(@Valid @RequestBody DroneDTO requestDTO) {
        DroneDTO responseDTO = droneService.createDrone(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping(path = "/state/{state}")
    public ResponseEntity<DroneListDTO> getDronesOfGivenState(@PathVariable("state") String state) {
        List<DroneDTO> drones = droneService.getDronesForGivenState(state);

        DroneListDTO responseDTO = new DroneListDTO();
        responseDTO.setDrones(drones);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{serial_number}/medications")
    public ResponseEntity<MedicationListDTO> getLoadedMedications(
            @PathVariable("serial_number") String serialNumber) {
        List<MedicationDTO> medications = droneService.getLoadedMedications(serialNumber);

        MedicationListDTO responseDTO = new MedicationListDTO();
        responseDTO.setMedications(medications);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{serial_number}/battery-level")
    public ResponseEntity<BatteryCapacityDTO> getBatteryLevel(@PathVariable("serial_number") String serialNumber) {
        BatteryCapacityDTO responseDTO = droneService.getBatteryLevel(serialNumber);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/{serial_number}/medications/add")
    public ResponseEntity<MedicationListDTO> addMedications(@Valid @RequestBody MedicationListDTO requestDTO,
                                                            @PathVariable("serial_number") String serialNumber) {
        List<MedicationDTO> medications = droneService.addMedications(serialNumber, requestDTO.getMedications());

        MedicationListDTO responseDTO = new MedicationListDTO();
        responseDTO.setMedications(medications);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
