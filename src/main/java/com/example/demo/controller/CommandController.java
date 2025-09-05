package com.example.demo.controller;

import com.example.demo.mqtt.MqttGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/commands")
public class CommandController {
    
    private final MqttGatewayService mqttService;
    
    public CommandController(MqttGatewayService mqttService) {
        this.mqttService = mqttService;
    }
    
    @PostMapping("/{deviceId}/{command}")
    public ResponseEntity<String> sendCommand(@PathVariable String deviceId, @PathVariable String command) {
        
        System.out.println("🚀 Commande reçue: " + deviceId + " -> " + command);
        
        // Valider la commande
        if (!command.equalsIgnoreCase("ON") && !command.equalsIgnoreCase("OFF")) {
            return ResponseEntity.badRequest().body("Commande invalide");
        }
        
        // Valider l'appareil
        if (!deviceId.matches("lampe1|lampe2|ventilateur")) {
            return ResponseEntity.badRequest().body("Appareil non reconnu");
        }
        
        try {
            // Publier via MQTT
            mqttService.publishCommand(deviceId, command.toUpperCase());
            System.out.println("✅ Commande MQTT publiée avec succès");
            return ResponseEntity.ok("Commande envoyée");
        } catch (Exception e) {
            System.err.println("❌ Erreur MQTT: " + e.getMessage());
            return ResponseEntity.status(500).body("Erreur serveur");
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backend fonctionnel");
    }
}

