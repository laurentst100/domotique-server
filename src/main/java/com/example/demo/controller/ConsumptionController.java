package com.example.demo.controller;

import com.example.demo.model.AlertData;
import com.example.demo.model.ConsumptionData;
import com.example.demo.service.ConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consumption")
@CrossOrigin(origins = "*")
public class ConsumptionController {
    
    @Autowired
    private ConsumptionService consumptionService;
    
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentConsumption() {
        try {
            Map<String, Object> currentData = consumptionService.getCurrentConsumption();
            return ResponseEntity.ok(currentData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                Map.of("error", "Erreur récupération données actuelles", "details", e.getMessage())
            );
        }
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<ConsumptionData> getLatestByDevice(@PathVariable String deviceId) {
        try {
            ConsumptionData data = consumptionService.getLatestByDevice(deviceId);
            if (data != null) {
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/history/{deviceId}")
    public ResponseEntity<List<ConsumptionData>> getDeviceHistory(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "24") int hours) {
        try {
            if (hours <= 0 || hours > 168) { // Max 7 jours
                return ResponseEntity.badRequest().build();
            }
            
            List<ConsumptionData> history = consumptionService.getDeviceHistory(deviceId, hours);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
    
    @GetMapping("/average/{deviceId}")
    public ResponseEntity<Map<String, Object>> getAveragePower(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "24") int hours) {
        try {
            Double avgPower = consumptionService.getAveragePower(deviceId, hours);
            return ResponseEntity.ok(Map.of(
                "deviceId", deviceId,
                "averagePower", avgPower,
                "period", hours + "h",
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                Map.of("error", "Erreur calcul moyenne", "details", e.getMessage())
            );
        }
    }
    
    @GetMapping("/alerts/recent")
    public ResponseEntity<List<AlertData>> getRecentAlerts() {
        try {
            List<AlertData> alerts = consumptionService.getRecentAlerts();
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
    
    @GetMapping("/alerts/unacknowledged")
    public ResponseEntity<List<AlertData>> getUnacknowledgedAlerts() {
        try {
            List<AlertData> alerts = consumptionService.getUnacknowledgedAlerts();
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
    
    @PostMapping("/alerts/{alertId}/acknowledge")
    public ResponseEntity<Map<String, Object>> acknowledgeAlert(@PathVariable Long alertId) {
        try {
            consumptionService.acknowledgeAlert(alertId);
            return ResponseEntity.ok(Map.of(
                "message", "Alerte acquittée avec succès",
                "alertId", alertId,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                Map.of("error", "Erreur acquittement alerte", "details", e.getMessage())
            );
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "service", "Consumption Monitoring",
            "timestamp", System.currentTimeMillis()
        ));
    }
}
