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

    public void publishCommand(String deviceId, String command) {
        String topic = String.format(cmdTopicTemplate, deviceId);
        
        // --- LOG DE DÉBOGAGE ---
        System.out.println("Dans MqttGatewayService : Préparation de l'envoi du message...");
        System.out.println("Topic cible : " + topic);
        System.out.println("Payload (message) : " + command);

        var msg = MessageBuilder.withPayload(command)
                .setHeader("mqtt_topic", topic)
                .build();
        
        mqttOutboundChannel.send(msg);
        System.out.println("-> Message envoyé au canal MQTT.");
    }
}
