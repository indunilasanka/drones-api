package com.assignment.drones.repository;

import com.assignment.drones.model.domain.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Define repository operations for Medication API
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
