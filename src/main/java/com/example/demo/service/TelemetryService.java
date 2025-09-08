package com.example.demo.service;

import com.example.demo.model.TelemetryData;
import com.example.demo.dto.HourlyPowerSummary;
import com.example.demo.repository.TelemetryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TelemetryService {

    @Autowired
    private TelemetryRepository telemetryRepository;

    public TelemetryData saveTelemetry(TelemetryData telemetryData) {
        // S'assurer que le timestamp est bien celui du serveur à la réception
        telemetryData.setTimestamp(Instant.now());
        return telemetryRepository.save(telemetryData);
    }

    public List<TelemetryData> getLatestTelemetryForAllDevices() {
        return telemetryRepository.findLatestForEachDevice();
    }
    public List<HourlyPowerSummary> getHourlySummaryForLast24Hours() {
    // On définit la période (les dernières 24 heures)
    Instant since = Instant.now().minus(24, ChronoUnit.HOURS);
    
    // On appelle la nouvelle méthode du repository que tu as créée
    return telemetryRepository.findHourlyPowerSummary(since);
  }
}
