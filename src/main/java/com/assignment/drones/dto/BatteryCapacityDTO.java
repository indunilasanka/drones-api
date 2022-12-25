package com.assignment.drones.dto;

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
