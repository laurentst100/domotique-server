// src/main/java/com/example/demo/service/MqttGateway.java
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class MqttGateway {
    public void publishCommand(String deviceId, String command) {
        // TODO: implémenter quand MQTT sera prêt
        System.out.printf("Simulate publish -> %s : %s%n", deviceId, command);
    }
}
