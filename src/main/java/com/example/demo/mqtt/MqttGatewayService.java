package com.example.demo.mqtt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class MqttGatewayService {
    private final MessageChannel mqttOutboundChannel;

    @Value("${mqtt.topic.cmd}")
    private String cmdTopicTemplate;

    public MqttGatewayService(@Qualifier("mqttOutboundChannel") MessageChannel mqttOutboundChannel) {
        this.mqttOutboundChannel = mqttOutboundChannel;
    }

// Dans MqttGatewayService.java
public void publishCommand(String deviceId, String command) {
    String topic = String.format(cmdTopicTemplate, deviceId);
    
    // AJOUTER CES LOGS
    System.out.println("📤 === PUBLICATION MQTT ===");
    System.out.println("Topic exact: [" + topic + "]");
    System.out.println("Message exact: [" + command + "]");
    System.out.println("Template utilisé: [" + cmdTopicTemplate + "]");
    
    var message = MessageBuilder.withPayload(command)
            .setHeader("mqtt_topic", topic)
            .build();
    
    boolean sent = mqttOutboundChannel.send(message);
    System.out.println(sent ? "✅ Message envoyé au broker" : "❌ Échec envoi");
    System.out.println("========================");
}


    // Ajouter ces méthodes dans ton MqttGatewayService existant

public void publishMessage(String topic, String message) {
    try {
        var mqttMessage = MessageBuilder.withPayload(message)
                .setHeader("mqtt_topic", topic)
                .build();
        
        boolean sent = mqttOutboundChannel.send(mqttMessage);
        
        if (sent) {
            System.out.println("✅ Message publié sur " + topic);
        } else {
            System.err.println("❌ Échec publication sur " + topic);
        }
    } catch (Exception e) {
        System.err.println("❌ Erreur MQTT: " + e.getMessage());
    }
}

public void publishAlert(String deviceId, String alertType, Double currentValue, Double threshold) {
    String alertTopic = "home/devices/esp32-1/" + deviceId + "/alert";
    
    org.json.JSONObject payload = new org.json.JSONObject();
    payload.put("type", alertType);
    payload.put("deviceId", deviceId);
    payload.put("current", currentValue);
    payload.put("threshold", threshold);
    payload.put("timestamp", System.currentTimeMillis());
    payload.put("severity", "HIGH");
    
    publishMessage(alertTopic, payload.toString());
}


}
