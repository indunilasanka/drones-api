package com.assignment.drones.repository;

import com.assignment.drones.model.domain.Drone;
import com.assignment.drones.model.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Define repository operations for drone API
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    List<Drone> findByState(State state);
}
