package com.javatechie.util;

import com.javatechie.entity.User;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.List;

public class AlertNotify {

    public static void senMessage(List<User> users, String message) {

    }

    private static MqttConnectOptions createOption() {
//        try {
//            MqttConnectOptions connectOptions = new MqttConnectOptions();
//            connectOptions.setUserName("server");
//            connectOptions.setPassword(probeOption.getPassword().toCharArray());
//            connectOptions.setKeepAliveInterval(probeOption.getKeepAlive());
//            connectOptions.setCleanSession(probeOption.getCleanSession());
//            connectOptions.setAutomaticReconnect(true);
//            return connectOptions;
//        }
//        catch (Exception e) {
////            System.out.println("Create connection error");
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }
}
