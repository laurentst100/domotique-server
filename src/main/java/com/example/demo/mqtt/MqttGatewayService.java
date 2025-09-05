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

}
