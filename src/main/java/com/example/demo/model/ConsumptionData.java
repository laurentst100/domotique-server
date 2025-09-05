package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consumption_data")
public class ConsumptionData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String deviceId;
    
    @Column(nullable = false)
    private Double current; // Courant en ampères
    
    @Column(nullable = false)
    private Double power; // Puissance en watts
    
    @Column(nullable = false)
    private Double voltage; // Tension secteur (230V)
    
    @Column(nullable = false)
    private Long timestamp;
    
    @Column
    private String status = "NORMAL"; // NORMAL, ALERT
    
    // Constructeurs
    public ConsumptionData() {}
    
    public ConsumptionData(String deviceId, Double current, Double power, Double voltage, Long timestamp) {
        this.deviceId = deviceId;
        this.current = current;
        this.power = power;
        this.voltage = voltage;
        this.timestamp = timestamp;
        this.status = "NORMAL";
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public Double getCurrent() {
        return current;
    }
    
    public void setCurrent(Double current) {
        this.current = current;
    }
    
    public Double getPower() {
        return power;
    }
    
    public void setPower(Double power) {
        this.power = power;
    }
    
    public Double getVoltage() {
        return voltage;
    }
    
    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "ConsumptionData{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", current=" + current +
                ", power=" + power +
                ", voltage=" + voltage +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }
}
