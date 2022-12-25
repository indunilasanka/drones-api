package com.assignment.drones.model.dto;

import java.math.BigDecimal;

public class BatteryCapacityDTO {
    private BigDecimal batteryCapacity;

    public BigDecimal getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(BigDecimal batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
}
