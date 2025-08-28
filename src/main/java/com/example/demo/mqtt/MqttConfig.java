package com.example.demo.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    @Value("${mqtt.brokerUri}")
    private String brokerUri;

    @Value("${mqtt.clientId.out}")
    private String clientIdOut;

    @Value("${mqtt.clientId.in}")
    private String clientIdIn;

    @Value("${mqtt.topic.state}")
    private String stateTopic;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setServerURIs(new String[]{brokerUri});
        opts.setAutomaticReconnect(true);
        opts.setCleanSession(true);
        opts.setConnectionTimeout(10);
        return opts;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(MqttConnectOptions options) {
        DefaultMqttPahoClientFactory f = new DefaultMqttPahoClientFactory();
        f.setConnectionOptions(options);
        return f;
    }

    // Canal pour messages sortants (API -> MQTT)
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // Handler de publication (topic fourni à l'envoi)
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler h = new MqttPahoMessageHandler(clientIdOut, factory);
        h.setAsync(true);
        return h;
    }

    // Canal pour messages entrants (MQTT -> API)
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    // Abonnement aux états
    @Bean
    public MessageProducer mqttInbound(MqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientIdIn, factory, stateTopic);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }
}