package com.assignment.drones.service.impl;

import com.assignment.drones.dto.BatteryCapacityDTO;
import com.assignment.drones.dto.DroneDTO;
import com.assignment.drones.dto.MedicationDTO;
import com.assignment.drones.exception.RuntimeValidationException;
import com.assignment.drones.model.Drone;
import com.assignment.drones.model.Medication;
import com.assignment.drones.model.State;
import com.assignment.drones.repository.DroneRepository;
import com.assignment.drones.repository.MedicationRepository;
import com.assignment.drones.service.DroneService;
import com.assignment.drones.util.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.assignment.drones.util.Constants.LOW_BATTERY_CAPACITY_THRESHOLD;

@Service
@Transactional
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    @Override
    public DroneDTO createDrone(DroneDTO dto) {
        Drone drone = droneRepository.save(Mapper.mapToDrone(dto));
        return Mapper.mapToDroneDTO(drone);
    }

    @Override
    public List<MedicationDTO> addMedications(String serialNumber, List<MedicationDTO> medications) {
        Optional<Drone> optionalDrone = droneRepository.findById(serialNumber);
        if (optionalDrone.isEmpty()) {
            throw new EntityNotFoundException(String.format("No drone exists for the given serial number: {}", serialNumber));
        }

        Drone drone = optionalDrone.get();

        if (drone.getBatteryCapacity().compareTo(BigDecimal.valueOf(LOW_BATTERY_CAPACITY_THRESHOLD)) >= 0) {
            BigDecimal totalWeight = drone.getMedications().stream().map(Medication::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);

            drone.setState(State.LOADING);
            droneRepository.save(drone);

            for (MedicationDTO dto : medications) {
                Medication medication = Mapper.mapToMedication(dto);

                if (drone.getWeightLimit().compareTo(totalWeight.add(medication.getWeight())) > 0) {
                    medication.setDrone(drone);
                    medication = medicationRepository.save(medication);
                    drone.getMedications().add(medication);
                    totalWeight = totalWeight.add(medication.getWeight());
                } else {
                    throw new RuntimeValidationException("Medications loading failed since drone weight limit exceeded");
                }
            }

            drone.setState(State.LOADED);
            drone = droneRepository.save(drone);
            return Mapper.mapMedicationListToDTOs(drone.getMedications());
        } else {
            throw new RuntimeValidationException("Drone battery level is below the required margin to load medications");
        }
    }

    @Override
    public List<MedicationDTO> getLoadedMedications(String serialNumber) {
        Optional<Drone> optionalDrone = droneRepository.findById(serialNumber);
        if (optionalDrone.isEmpty()) {
            throw new EntityNotFoundException(String.format("No drone exists for the given serial number: {}", serialNumber));
        }

        Drone drone = optionalDrone.get();
        return Mapper.mapMedicationListToDTOs(drone.getMedications());
    }

    @Override
    public BatteryCapacityDTO getBatteryLevel(String serialNumber) {
        Optional<Drone> optionalDrone = droneRepository.findById(serialNumber);
        if (optionalDrone.isEmpty()) {
            throw new EntityNotFoundException(String.format("No drone exists for the given serial number: {}", serialNumber));
        }

        Drone drone = optionalDrone.get();
        BatteryCapacityDTO batteryLevelDTO = new BatteryCapacityDTO();
        batteryLevelDTO.setBatteryCapacity(drone.getBatteryCapacity());
        return batteryLevelDTO;
    }

    @Override
    public List<DroneDTO> getDronesForGivenState(String state) {
        List<Drone> drones = droneRepository.findByState(State.valueOf(state.toUpperCase()));
        return Mapper.mapDroneListToDTOs(drones);
    }

    @Override
    public List<DroneDTO> getAllDrones() {
        List<Drone> drones = droneRepository.findAll();
        return Mapper.mapDroneListToDTOs(drones);
    }

}
