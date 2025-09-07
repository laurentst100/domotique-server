package com.example.demo.mqtt;

import com.example.demo.model.TelemetryData; // Assure-toi que c'est bien le bon package !
import com.example.demo.service.TelemetryService; // Tu auras besoin de ce service
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.broker.username}")
    private String username;

    @Value("${mqtt.broker.password}")
    private String password;

    @Autowired
    private TelemetryService telemetryService; // Injection du service pour sauvegarder

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    // --- CANAL POUR LES MESSAGES SORTANTS (COMMANDES VERS ESP32) ---
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler("backend-render-client", mqttClientFactory());
        handler.setAsync(true);
        handler.setDefaultQos(1); // Garantir la livraison
        return handler;
    }

    // --- FLUX POUR LES MESSAGES ENTRANTS (TÉLÉMÉTRIE DE L'ESP32) ---
    @Bean
    public IntegrationFlow mqttInboundFlow() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "backend-telemetry-consumer", // ID client unique pour cet adaptateur
                mqttClientFactory(),
                "home/devices/esp32-1/+/telemetry" // S'abonner à tous les topics de télémétrie
        );
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);

        return IntegrationFlows.from(adapter)
                .handle(message -> {
                    try {
                        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
                        String payload = message.getPayload().toString();
                        System.out.println("📊 Télémétrie reçue: " + topic + " -> " + payload);

                        // Extraire l'ID de l'appareil depuis le topic
                        String[] topicParts = topic.split("/");
                        String deviceId = topicParts[3];

                        // Parser le JSON
                        JSONObject json = new JSONObject(payload);
                        TelemetryData data = new TelemetryData();
                        data.setDeviceId(deviceId);
                        data.setStatus(json.getString("status"));
                        data.setCurrent(json.getDouble("current"));
                        data.setPower(json.getDouble("power"));

                        // Sauvegarder les données via le service
                        telemetryService.saveTelemetry(data);

                    } catch (Exception e) {
                        System.err.println("❌ Erreur traitement télémétrie MQTT: " + e.getMessage());
                    }
                })
                .get();
    }
}
