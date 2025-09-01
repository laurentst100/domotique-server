import com.example.demo.mqtt.MqttGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommandController {
    private final MqttGatewayService mqtt;

    public CommandController(MqttGatewayService mqtt) {
        this.mqtt = mqtt;
    }

    @PostMapping("/commands/{deviceId}/{command}")
    public ResponseEntity<String> receiveCommand(@PathVariable String deviceId, @PathVariable String command) {
        try {
            mqtt.publishCommand(deviceId, command);
            return ResponseEntity.ok("Commande '" + command + "' publiée !");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Erreur MQTT : " + ex.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running!");
    }
}
