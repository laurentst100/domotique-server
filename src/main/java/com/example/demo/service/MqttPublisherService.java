package com.example.demo.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.net.ssl.SSLSocketFactory;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class MqttPublisherService {

  private static final String URI = "ssl://773d8058583a498189617f4956c19598.s1.eu.hivemq.cloud:8883"; // host HiveMQ Cloud
  private MqttClient client;

  public MqttPublisherService() {
    try {
      client = new MqttClient(URI, MqttClient.generateClientId());
      MqttConnectOptions opts = new MqttConnectOptions();
      opts.setUserName("api-server");                       // remplace par ton username HiveMQ (API)
      opts.setPassword("unhcr1010AZERTY".toCharArray());     // remplace par ton mot de passe HiveMQ (API)
      opts.setSocketFactory(SSLSocketFactory.getDefault()); // TLS requis en 8883
      client.connect(opts);
    } catch (MqttException e) {
      throw new IllegalStateException("MQTT connect failed", e);
    }
  }

  public void sendCommand(String deviceId, String cmd) {
    try {
      String topic = "home/devices/" + deviceId + "/cmd";
      client.publish(topic, cmd.getBytes(StandardCharsets.UTF_8), 1, false);
    } catch (MqttException e) {
      throw new IllegalStateException("MQTT publish failed", e);
    }
  }
}
