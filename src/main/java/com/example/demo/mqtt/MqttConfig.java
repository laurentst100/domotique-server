package com.example.demo.mqtt;

import com.example.demo.model.TelemetryData;
import com.example.demo.service.TelemetryService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
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
    private TelemetryService telemetryService;

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

    // --- PARTIE SORTANTE (INCHANGÉE) ---
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler("backend-render-client", mqttClientFactory());
        handler.setAsync(true);
        handler.setDefaultQos(1);
        return handler;
    }

    // --- PARTIE ENTRANTE (MODIFIÉE POUR ÉVITER IntegrationFlows) ---

    // 1. On crée le "tuyau" d'entrée
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // 2. On branche l'adaptateur MQTT à l'entrée du tuyau
    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("backend-telemetry-consumer",
                                                        mqttClientFactory(),
                                                        "home/devices/esp32-1/+/telemetry");
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel()); // On connecte l'adaptateur au tuyau
        return adapter;
    }

    // 3. On place l'"ouvrier" au bout du tuyau pour traiter les messages
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            try {
                String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
                String payload = message.getPayload().toString();
                System.out.println("📊 Télémétrie reçue: " + topic + " -> " + payload);

                String[] topicParts = topic.split("/");
                String deviceId = topicParts[3];

                JSONObject json = new JSONObject(payload);
                TelemetryData data = new TelemetryData();
                data.setDeviceId(deviceId);
                data.setStatus(json.getString("status"));
                data.setCurrent(json.getDouble("current"));
                data.setPower(json.getDouble("power"));

                telemetryService.saveTelemetry(data);
            } catch (Exception e) {
                System.err.println("❌ Erreur traitement télémétrie MQTT: " + e.getMessage());
            }
        };
    }
}
