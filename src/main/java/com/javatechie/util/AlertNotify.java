package com.javatechie.util;

import com.javatechie.entity.User;
import com.javatechie.util.constain.ConstVariable;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

public class AlertNotify {
    private static MqttClient client = null;

    public static void senMessage(String message) {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            MqttConnectOptions connectOptions = createOption();
            if (client == null || !client.isConnected()) {
                client = new MqttClient(ConstVariable.BROKER_URL, "server", persistence);
                client.connect(connectOptions);
            }
            client.subscribe("buy", (topic, mess) -> {
                String res = new String(mess.getPayload());
                System.out.println(res);
            });
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            client.publish("buy", mqttMessage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MqttConnectOptions createOption() {
        try {
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName("server");
            connectOptions.setPassword("1234".toCharArray());
            connectOptions.setKeepAliveInterval(60);
            connectOptions.setCleanSession(true);
            connectOptions.setAutomaticReconnect(true);
            return connectOptions;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
