package com.assignment.drones.dto;

import com.assignment.drones.model.Model;
import com.assignment.drones.model.State;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class DroneDTO {
    @Size(max = 100, message = "Serial number should not be greater than 100 characters")
    @NotEmpty(message = "Serial number should not be empty")
    private String serialNumber;
    private Model model;
    @Max(value = 500, message = "Weight limit should not be greater than 500")
    private BigDecimal weightLimit;
    @Max(value = 100, message = "Battery capacity should not be greater than 100")
    private BigDecimal batteryCapacity;
    private State state;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public BigDecimal getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(BigDecimal weightLimit) {
        this.weightLimit = weightLimit;
    }

    public BigDecimal getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(BigDecimal batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
