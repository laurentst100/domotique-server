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
        
        // --- LOG DE DÉBOGAGE ---
        System.out.println("--- Requête HTTP reçue sur /commands ---");
        System.out.println("Device ID: " + deviceId);
        System.out.println("Commande: " + command);
        
        try {
            System.out.println("Tentative de publication sur MQTT via MqttGatewayService...");
            
            // Appel de ton service pour publier la commande
            mqttService.publishCommand(deviceId, command);
            
            System.out.println("-> Publication MQTT effectuée avec succès !");
            return ResponseEntity.ok("Commande '" + command + "' publiée sur MQTT.");

        } catch (Exception ex) {
            System.err.println("ERREUR lors de la publication MQTT: " + ex.getMessage());
            ex.printStackTrace(); // Affiche la trace complète de l'erreur dans les logs
            return ResponseEntity.status(500).body("Erreur interne lors de la publication MQTT.");
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running!");
    }
}
