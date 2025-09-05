package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alert_data")
public class AlertData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String deviceId;
    
    @Column(nullable = false)
    private String type; // OVERCURRENT, UNDERVOLTAGE, etc.
    
    @Column(nullable = false)
    private Double currentValue;
    
    @Column(nullable = false)
    private Double thresholdValue;
    
    @Column(nullable = false)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(nullable = false)
    private Long timestamp;
    
    @Column(length = 500)
    private String message;
    
    @Column
    private Boolean acknowledged = false;
    
    // Constructeurs
    public AlertData() {}
    
    public AlertData(String deviceId, String type, Double currentValue, Double thresholdValue, String severity, String message) {
        this.deviceId = deviceId;
        this.type = type;
        this.currentValue = currentValue;
        this.thresholdValue = thresholdValue;
        this.severity = severity;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.acknowledged = false;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Double getCurrentValue() {
        return currentValue;
    }
    
    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }
    
    public Double getThresholdValue() {
        return thresholdValue;
    }
    
    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean getAcknowledged() {
        return acknowledged;
    }
    
    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
    
    @Override
    public String toString() {
        return "AlertData{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", type='" + type + '\'' +
                ", currentValue=" + currentValue +
                ", thresholdValue=" + thresholdValue +
                ", severity='" + severity + '\'' +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", acknowledged=" + acknowledged +
                '}';
    }
}
