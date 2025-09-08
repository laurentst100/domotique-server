package com.example.demo.repository;

import com.example.demo.dto.HourlyPowerSummary;
import com.example.demo.model.TelemetryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelemetryRepository extends JpaRepository<TelemetryData, Long> {

    // Trouve la dernière entrée de télémétrie pour un appareil spécifique
    Optional<TelemetryData> findTopByDeviceIdOrderByTimestampDesc(String deviceId);

    // Trouve les dernières entrées pour une liste d'appareils (plus avancé)
    @Query("SELECT t FROM TelemetryData t WHERE t.id IN (SELECT MAX(t2.id) FROM TelemetryData t2 GROUP BY t2.deviceId)")
    List<TelemetryData> findLatestForEachDevice();
    @Query("SELECT new com.example.demo.dto.HourlyPowerSummary(" +
           "   CAST(function('date_trunc', 'hour', t.timestamp) AS Instant), " +
           "   SUM(t.power)" +
           ") " +
           "FROM TelemetryData t " +
           "WHERE t.timestamp >= :since " +
           "GROUP BY function('date_trunc', 'hour', t.timestamp) " +
           "ORDER BY function('date_trunc', 'hour', t.timestamp) ASC")
    List<HourlyPowerSummary> findHourlyPowerSummary(@Param("since") Instant since);

}
