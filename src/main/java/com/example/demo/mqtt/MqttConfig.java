package com.example.demo.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
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

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { brokerUrl });
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true); 
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

// Dans MqttConfig.java
    // Ajouter cette méthode dans ton MqttConfig existant

@Bean
public IntegrationFlow mqttConsumptionInboundFlow(MqttPahoClientFactory clientFactory) {
    return IntegrationFlows
        .from(Mqtt.messageDrivenChannelAdapter(
            clientFactory,
            "backend-consumption-consumer",
            "home/devices/esp32-1/+/consumption"))
        .handle(message -> {
            try {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String payload = message.getPayload().toString();
                
                System.out.println("📊 Données consommation reçues: " + topic + " -> " + payload);
                
                // Parser le JSON
                org.json.JSONObject json = new org.json.JSONObject(payload);
                
                ConsumptionData data = new ConsumptionData();
                data.setDeviceId(json.getString("deviceId"));
                data.setCurrent(json.getDouble("current"));
                data.setPower(json.getDouble("power"));
                data.setVoltage(json.getDouble("voltage"));
                data.setTimestamp(json.getLong("timestamp"));
                
                // Sauvegarder et vérifier les seuils
                consumptionService.saveConsumptionData(data);
                
            } catch (Exception e) {
                System.err.println("❌ Erreur traitement consommation: " + e.getMessage());
            }
        })
        .get();
}

// Dans MqttConfig.java


@Bean
@ServiceActivator(inputChannel = "mqttOutboundChannel")
public MessageHandler mqttOutbound() {
    // Client ID unique pour éviter les conflits
    MqttPahoMessageHandler handler = new MqttPahoMessageHandler("backend-render-client", mqttClientFactory());
    handler.setAsync(true);
    
    // --- CORRECTION FINALE : On force la Qualité de Service à 1 ---
    // Cela garantit que le message sera délivré au broker.
    handler.setDefaultQos(1);
    
    return handler;
}


}
