package com.example.demo.service;

import com.example.demo.model.AlertData;
import com.example.demo.model.ConsumptionData;
import com.example.demo.repository.AlertRepository;
import com.example.demo.repository.ConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConsumptionService {
    
    private static final Logger logger = LoggerFactory.getLogger(ConsumptionService.class);
    
    @Autowired
    private ConsumptionRepository consumptionRepository;
    
    @Autowired
    private AlertRepository alertRepository;
    
    @Autowired
    private AlertService alertService;
    
    // Seuils d'alerte par appareil (configurable)
    private final Map<String, Double> deviceThresholds = Map.of(
        "lampe1", 2.0,      // 2A max
        "lampe2", 2.0,      // 2A max  
        "ventilateur", 3.0  // 3A max
    );
    
    public void saveConsumptionData(ConsumptionData data) {
        try {
            // Vérifier les seuils avant sauvegarde
            checkThresholds(data);
            
            // Sauvegarder
            ConsumptionData saved = consumptionRepository.save(data);
            logger.info("💾 Données sauvegardées: {} = {}A ({}W) - ID: {}", 
                       data.getDeviceId(), data.getCurrent(), data.getPower(), saved.getId());
            
        } catch (Exception e) {
            logger.error("❌ Erreur sauvegarde consommation: {}", e.getMessage(), e);
        }
    }
    
    private void checkThresholds(ConsumptionData data) {
        String deviceId = data.getDeviceId();
        Double threshold = deviceThresholds.get(deviceId);
        
        if (threshold != null && data.getCurrent() > threshold) {
            // Créer et sauvegarder l'alerte
            AlertData alert = new AlertData(
                deviceId,
                "OVERCURRENT",
                data.getCurrent(),
                threshold,
                "HIGH",
                String.format("Surconsommation détectée: %.2fA > %.2fA (%.0fW)", 
                             data.getCurrent(), threshold, data.getPower())
            );
            
            AlertData savedAlert = alertRepository.save(alert);
            logger.warn("⚠️ ALERTE créée {}: {} - ID: {}", deviceId, alert.getMessage(), savedAlert.getId());
            
            // Déclencher la notification
            alertService.triggerAlert(savedAlert);
            
            // Marquer les données comme en alerte
            data.setStatus("ALERT");
        }
    }
    
    public Map<String, Object> getCurrentConsumption() {
        List<ConsumptionData> latestData = consumptionRepository.findLatestForAllDevices();
        
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("devices", latestData);
        
        // Calculer totaux
        double totalCurrent = latestData.stream()
            .mapToDouble(ConsumptionData::getCurrent)
            .sum();
        double totalPower = latestData.stream()
            .mapToDouble(ConsumptionData::getPower)
            .sum();
        
        result.put("totalCurrent", Math.round(totalCurrent * 100.0) / 100.0);
        result.put("totalPower", Math.round(totalPower * 10.0) / 10.0);
        result.put("deviceCount", latestData.size());
        
        logger.info("📊 Consommation actuelle: {}A total, {}W total, {} appareils", 
                   result.get("totalCurrent"), result.get("totalPower"), latestData.size());
        
        return result;
    }
    
    public List<ConsumptionData> getDeviceHistory(String deviceId, int hours) {
        long fromTimestamp = System.currentTimeMillis() - (hours * 60 * 60 * 1000L);
        List<ConsumptionData> history = consumptionRepository.findByDeviceIdAndTimestampGreaterThan(deviceId, fromTimestamp);
        
        logger.info("📈 Historique {} : {} enregistrements sur {}h", deviceId, history.size(), hours);
        return history;
    }
    
    public List<AlertData> getRecentAlerts() {
        long last24Hours = System.currentTimeMillis() - (24 * 60 * 60 * 1000L);
        List<AlertData> alerts = alertRepository.findRecentAlerts(last24Hours);
        
        logger.info("🚨 Alertes récentes: {} alertes sur 24h", alerts.size());
        return alerts;
    }
    
    public List<AlertData> getUnacknowledgedAlerts() {
        List<AlertData> alerts = alertRepository.findUnacknowledgedAlerts();
        logger.info("🔔 Alertes non acquittées: {}", alerts.size());
        return alerts;
    }
    
    public void acknowledgeAlert(Long alertId) {
        alertRepository.findById(alertId).ifPresent(alert -> {
            alert.setAcknowledged(true);
            alertRepository.save(alert);
            logger.info("✅ Alerte acquittée: {} - {}", alertId, alert.getMessage());
        });
    }
    
    public ConsumptionData getLatestByDevice(String deviceId) {
        return consumptionRepository.findLatestByDeviceId(deviceId).orElse(null);
    }
    
    public Double getAveragePower(String deviceId, int hours) {
        long fromTimestamp = System.currentTimeMillis() - (hours * 60 * 60 * 1000L);
        Double avgPower = consumptionRepository.findAveragePowerByDeviceIdAndPeriod(deviceId, fromTimestamp);
        return avgPower != null ? Math.round(avgPower * 10.0) / 10.0 : 0.0;
    }
}
