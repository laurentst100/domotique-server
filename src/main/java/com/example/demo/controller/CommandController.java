package com.example.demo.controller;

import com.example.demo.mqtt.MqttGatewayService; // Assure-toi que le chemin est correct
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// Les URL commenceront à la racine, ex: /commands/...
@RequestMapping("/")
public class CommandController {

    private final MqttGatewayService mqttService;

    public CommandController(MqttGatewayService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/commands/{deviceId}/{command}")
    public ResponseEntity<String> receiveCommand(@PathVariable String deviceId, @PathVariable String command) {
        try {
            // On utilise ton service pour publier la commande
            mqttService.publishCommand(deviceId, command);
            return ResponseEntity.ok("Commande '" + command + "' publiée sur MQTT.");
        } catch (Exception ex) {
            // Log l'erreur pour le débogage
            System.err.println("Erreur lors de la publication MQTT: " + ex.getMessage());
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running!");
    }
}
