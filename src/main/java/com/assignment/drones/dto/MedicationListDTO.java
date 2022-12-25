package com.assignment.drones.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class MedicationListDTO {
    @NotEmpty
    @Valid
    List<MedicationDTO> medications;

    public List<MedicationDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationDTO> medications) {
        this.medications = medications;
    }
}
