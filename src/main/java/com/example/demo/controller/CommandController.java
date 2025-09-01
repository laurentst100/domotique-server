package com.example.demo.controller;

import com.example.demo.mqtt.MqttGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommandController {

    private final MqttGatewayService mqttService;

    public CommandController(MqttGatewayService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/commands/{deviceId}/{command}")
    public ResponseEntity<String> receiveCommand(@PathVariable String deviceId, @PathVariable String command) {
        mqttService.publishCommand(deviceId, command); // "ON" ou "OFF"
        return ResponseEntity.ok("Commande '" + command + "' publiée !");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running!");
    }
}
