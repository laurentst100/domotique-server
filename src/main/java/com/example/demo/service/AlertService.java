package com.example.demo.service;

import com.example.demo.model.AlertData;
import com.example.demo.mqtt.MqttGatewayService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    
    @Autowired
    private MqttGatewayService mqttGatewayService;
    
    public void triggerAlert(AlertData alert) {
        try {
            // Publier l'alerte sur MQTT pour l'app mobile
            publishAlertToMqtt(alert);
            
            // Autres types de notifications peuvent être ajoutés ici
            // sendEmailAlert(alert);
            // sendSmsAlert(alert);
            
            logger.info("📢 Alerte déclenchée: {} - {}", alert.getDeviceId(), alert.getMessage());
            
        } catch (Exception e) {
            logger.error("❌ Erreur déclenchement alerte: {}", e.getMessage(), e);
        }
    }
    
    private void publishAlertToMqtt(AlertData alert) {
        try {
            // Créer le payload JSON de l'alerte
            JSONObject alertPayload = new JSONObject();
            alertPayload.put("id", alert.getId());
            alertPayload.put("type", alert.getType());
            alertPayload.put("deviceId", alert.getDeviceId());
            alertPayload.put("currentValue", alert.getCurrentValue());
            alertPayload.put("thresholdValue", alert.getThresholdValue());
            alertPayload.put("severity", alert.getSeverity());
            alertPayload.put("message", alert.getMessage());
            alertPayload.put("timestamp", alert.getTimestamp());
            alertPayload.put("acknowledged", alert.getAcknowledged());
            
            // Topic d'alerte général
            String alertTopic = "home/devices/esp32-1/alerts";
            
            // Topic spécifique à l'appareil
            String deviceAlertTopic = "home/devices/esp32-1/" + alert.getDeviceId() + "/alert";
            
            // Publier sur les deux topics
            mqttGatewayService.publishMessage(alertTopic, alertPayload.toString());
            mqttGatewayService.publishMessage(deviceAlertTopic, alertPayload.toString());
            
            logger.info("📡 Alerte MQTT publiée: {} -> {}", alertTopic, alert.getMessage());
            
        } catch (Exception e) {
            logger.error("❌ Erreur publication alerte MQTT: {}", e.getMessage(), e);
        }
    }
    
    // Méthodes futures pour autres types de notifications
    private void sendEmailAlert(AlertData alert) {
        // TODO: Implémentation email avec JavaMail
        logger.info("📧 Email alerte (TODO): {}", alert.getMessage());
    }
    
    private void sendSmsAlert(AlertData alert) {
        // TODO: Implémentation SMS avec Twilio ou autre
        logger.info("📱 SMS alerte (TODO): {}", alert.getMessage());
    }
}
