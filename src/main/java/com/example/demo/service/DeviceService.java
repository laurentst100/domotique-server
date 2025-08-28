// src/main/java/com/example/demo/service/DeviceService.java
package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceService {
    public List<String> listDevices() {
        // Temporaire: données factices
        return List.of("prise-salon", "lampe-chambre");
    }
}
