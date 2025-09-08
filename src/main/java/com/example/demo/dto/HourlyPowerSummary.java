package com.example.demo.dto;

import java.time.Instant;

public class HourlyPowerSummary {

    private Instant hour;
    private Double totalPower;

    // Constructeur important pour la requête JPA
    public HourlyPowerSummary(Instant hour, Double totalPower) {
        this.hour = hour;
        this.totalPower = totalPower;
    }

    // Getters et Setters
    public Instant getHour() {
        return hour;
    }

    public void setHour(Instant hour) {
        this.hour = hour;
    }

    public Double getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(Double totalPower) {
        this.totalPower = totalPower;
    }
}
