package com.example.demo.controller;

import com.example.demo.dto.HourlyPowerSummary;
import com.example.demo.model.TelemetryData;
import com.example.demo.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
@CrossOrigin(origins = "*") // Permet les appels depuis n'importe quelle origine (pour les tests)
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    /**
     * Endpoint pour que l'ESP32 envoie ses données de télémétrie.
     */
    @PostMapping
    public ResponseEntity<TelemetryData> receiveTelemetry(@Valid @RequestBody TelemetryData telemetryData) {
        TelemetryData savedData = telemetryService.saveTelemetry(telemetryData);
        return ResponseEntity.ok(savedData);
    }

    /**
     * Endpoint pour que l'application mobile récupère les dernières données de tous les appareils.
     */
    @GetMapping("/latest")
    public ResponseEntity<List<TelemetryData>> getLatestTelemetry() {
        List<TelemetryData> latestData = telemetryService.getLatestTelemetryForAllDevices();
        return ResponseEntity.ok(latestData);
    }
     @GetMapping("/summary/24h")
    public ResponseEntity<List<HourlyPowerSummary>> get24HourSummary() {
        List<HourlyPowerSummary> summary = telemetryService.getHourlySummaryForLast24Hours();
        return ResponseEntity.ok(summary);
    }
}
