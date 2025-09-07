package com.example.demo.repository;

import com.example.demo.moddel.TelemetryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelemetryRepository extends JpaRepository<TelemetryData, Long> {

    // Trouve la dernière entrée de télémétrie pour un appareil spécifique
    Optional<TelemetryData> findTopByDeviceIdOrderByTimestampDesc(String deviceId);

    // Trouve les dernières entrées pour une liste d'appareils (plus avancé)
    @Query("SELECT t FROM TelemetryData t WHERE t.id IN (SELECT MAX(t2.id) FROM TelemetryData t2 GROUP BY t2.deviceId)")
    List<TelemetryData> findLatestForEachDevice();
}
