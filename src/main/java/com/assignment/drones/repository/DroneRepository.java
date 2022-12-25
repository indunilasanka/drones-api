package com.assignment.drones.repository;

import com.assignment.drones.model.Drone;
import com.assignment.drones.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    List<Drone> findByState(State state);
}
