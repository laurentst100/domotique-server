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
        var msg = MessageBuilder.withPayload(command)
                .setHeader("mqtt_topic", topic)
                .build();
        mqttOutboundChannel.send(msg);
    }
}
