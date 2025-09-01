package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// Nous enlevons le préfixe /api global pour simplifier les tests
@RequestMapping("/")
public class CommandController {

    @PostMapping("/commands/{deviceId}/{command}")
    public ResponseEntity<String> receiveCommand(@PathVariable String deviceId, @PathVariable String command) {
        System.out.println("TEST POST: Commande '" + command + "' reçue pour l'appareil: " + deviceId);
        return ResponseEntity.ok("Commande '" + command + "' reçue.");
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        System.out.println("TEST GET: Le service de santé a été appelé.");
        return ResponseEntity.ok("Service is up and running!");
    }
}
