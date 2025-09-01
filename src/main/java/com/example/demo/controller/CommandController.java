package com.example.demo.controller;

import com.example.demo.service.MqttPublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commands")
public class CommandController {

  private final MqttPublisherService mqtt;

  public CommandController(MqttPublisherService mqtt) {
    this.mqtt = mqtt;
  }


@GetMapping("/health")
public String healthCheck() {
   return "Service is up and running!";
}

  @PostMapping("/{deviceId}/{cmd}")
  public ResponseEntity<String> send(@PathVariable String deviceId, @PathVariable String cmd) {
    mqtt.sendCommand(deviceId, cmd);
    return ResponseEntity.ok("queued");
  }
}
