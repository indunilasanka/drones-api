package com.assignment.drones.handler;

import com.assignment.drones.model.dto.DroneDTO;
import com.assignment.drones.service.DroneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.assignment.drones.util.Constants.SCHEDULER_DEFAULT_DELAY;

/**
 * Works as a scheduler to execute scheduled tasks
 */
@Component
public class Scheduler {
    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);
    private final DroneService droneService;

    public Scheduler(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(fixedDelay = SCHEDULER_DEFAULT_DELAY)
    public void checkBatteryCapacityOfDrones() {
        List<DroneDTO> drones = droneService.getAllDrones();

        logger.info("Time now: {}", LocalDateTime.now());
        for (DroneDTO dto : drones) {
            logger.info("Battery level of drone:{} is {}", dto.getSerialNumber(), dto.getBatteryCapacity());
        }
    }
}
