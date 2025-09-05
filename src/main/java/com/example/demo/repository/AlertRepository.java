package com.example.demo.repository;

import com.example.demo.model.AlertData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<AlertData, Long> {
    
    // Alertes récentes (ex: dernières 24h)
    @Query("SELECT a FROM AlertData a WHERE a.timestamp >= :fromTimestamp ORDER BY a.timestamp DESC")
    List<AlertData> findRecentAlerts(@Param("fromTimestamp") Long fromTimestamp);
    
    // Alertes non acquittées
    @Query("SELECT a FROM AlertData a WHERE a.acknowledged = false ORDER BY a.timestamp DESC")
    List<AlertData> findUnacknowledgedAlerts();
    
    // Alertes par appareil
    List<AlertData> findByDeviceIdOrderByTimestampDesc(String deviceId);
    
    // Alertes par sévérité
    List<AlertData> findBySeverityOrderByTimestampDesc(String severity);
    
    // Nombre d'alertes par sévérité et période
    @Query("SELECT a.severity, COUNT(a) FROM AlertData a WHERE a.timestamp >= :fromTimestamp GROUP BY a.severity")
    List<Object[]> countAlertsBySeverityAndPeriod(@Param("fromTimestamp") Long fromTimestamp);
    
    // Alertes par type
    List<AlertData> findByTypeOrderByTimestampDesc(String type);
    
    // Alertes critiques récentes
    @Query("SELECT a FROM AlertData a WHERE a.severity = 'HIGH' AND a.timestamp >= :fromTimestamp ORDER BY a.timestamp DESC")
    List<AlertData> findRecentCriticalAlerts(@Param("fromTimestamp") Long fromTimestamp);
}
