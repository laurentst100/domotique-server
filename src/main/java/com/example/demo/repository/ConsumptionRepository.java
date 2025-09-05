package com.example.demo.repository;

import com.example.demo.model.ConsumptionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionRepository extends JpaRepository<ConsumptionData, Long> {
    
    // Dernière mesure pour un appareil
    @Query("SELECT c FROM ConsumptionData c WHERE c.deviceId = :deviceId ORDER BY c.timestamp DESC")
    Optional<ConsumptionData> findLatestByDeviceId(@Param("deviceId") String deviceId);
    
    // Historique sur une période (timestamp en millisecondes)
    @Query("SELECT c FROM ConsumptionData c WHERE c.deviceId = :deviceId AND c.timestamp >= :fromTimestamp ORDER BY c.timestamp DESC")
    List<ConsumptionData> findByDeviceIdAndTimestampGreaterThan(@Param("deviceId") String deviceId, @Param("fromTimestamp") Long fromTimestamp);
    
    // Toutes les dernières mesures (une par appareil)
    @Query("SELECT c1 FROM ConsumptionData c1 WHERE c1.timestamp = (SELECT MAX(c2.timestamp) FROM ConsumptionData c2 WHERE c2.deviceId = c1.deviceId)")
    List<ConsumptionData> findLatestForAllDevices();
    
    // Consommation moyenne sur une période
    @Query("SELECT AVG(c.power) FROM ConsumptionData c WHERE c.deviceId = :deviceId AND c.timestamp >= :fromTimestamp")
    Double findAveragePowerByDeviceIdAndPeriod(@Param("deviceId") String deviceId, @Param("fromTimestamp") Long fromTimestamp);
    
    // Données par appareil triées par timestamp
    List<ConsumptionData> findByDeviceIdOrderByTimestampDesc(String deviceId);
    
    // Données récentes (dernières 24h par exemple)
    @Query("SELECT c FROM ConsumptionData c WHERE c.timestamp >= :fromTimestamp ORDER BY c.timestamp DESC")
    List<ConsumptionData> findRecentConsumption(@Param("fromTimestamp") Long fromTimestamp);
}
