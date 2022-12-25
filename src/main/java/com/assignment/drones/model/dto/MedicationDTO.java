package com.assignment.drones.model.dto;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class MedicationDTO {
    @Pattern(regexp = "[A-Za-z0-9_-]+", message = "Only letters, numbers, - and _ allowed for Code")
    private String code;
    @Pattern(regexp = "[A-Z0-9_]+", message = "Only upper case letters, numbers and _ allowed for Name")
    private String name;
    private BigDecimal weight;
    private byte[] image;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
