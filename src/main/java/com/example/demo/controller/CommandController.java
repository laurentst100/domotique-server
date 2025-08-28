package com.example.demo.controller;

import com.example.demo.mqtt.MqttGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commands")
public class CommandController {


private final MqttGatewayService mqtt;

public CommandController(MqttGatewayService mqtt) {
    this.mqtt = mqtt;
}

@PostMapping("/{deviceId}/{command}")
public ResponseEntity<String> send(@PathVariable String deviceId, @PathVariable String command) {
    mqtt.publishCommand(deviceId, command);
    return ResponseEntity.ok("queued");
}
}