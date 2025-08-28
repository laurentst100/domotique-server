package com.example.demo.mqtt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttInboundHandlers {


@Bean
@ServiceActivator(inputChannel = "mqttInboundChannel")
public MessageHandler handleStateMessages() {
    return message -> {
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        String payload = (String) message.getPayload();
        System.out.printf("MQTT INBOUND: %s -> %s%n", topic, payload);
        // TODO:
        // 1) Extraire deviceId depuis le topic (par ex. regex sur home/devices/{id}/state)
        // 2) Mettre à jour l’état en mémoire/DB
        // 3) Émettre vers WebSocket/SSE si souhaité
    };
}
}