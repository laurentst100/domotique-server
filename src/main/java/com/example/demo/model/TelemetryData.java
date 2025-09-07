package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "telemetry_data")
public class TelemetryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'ID de l'appareil ne peut pas être vide")
    @Column(nullable = false, updatable = false)
    private String deviceId;

    @NotBlank(message = "Le statut ne peut pas être vide")
    @Column(nullable = false)
    private String status; // "ON" ou "OFF"

    @NotNull(message = "La valeur du courant est requise")
    @Column(nullable = false)
    private Double current;

    @NotNull(message = "La valeur de la puissance est requise")
    @Column(nullable = false)
    private Double power;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    // Constructeurs, Getters et Setters

    public TelemetryData() {
        this.timestamp = Instant.now();
    }

    public TelemetryData(String deviceId, String status, Double current, Double power) {
        this.deviceId = deviceId;
        this.status = status;
        this.current = current;
        this.power = power;
        this.timestamp = Instant.now();
    }

    // Getters et Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getCurrent() { return current; }
    public void setCurrent(Double current) { this.current = current; }
    public Double getPower() { return power; }
    public void setPower(Double power) { this.power = power; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
