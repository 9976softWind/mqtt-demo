package com.hf;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author hf
 * 创建一个发布客户端类 PublishSample，该类将发布一条 Hello MQTT 消息至主题 mqtt/test。
 * @date 2024/6/14
 */
public class PublishSample {
    public static void main(String[] args) {
        String broker = "tcp://broker.emqx.io:1883";
        String topic = "mqtt/test";
        String username = "emqx";
        String password = "public";
        String clientid = "publish_client";
        String content = "Hello MQTT";
        int qos = 0;
        try {
            //MQTT客户端
            MqttClient mqttClient = new MqttClient(broker, clientid, new MemoryPersistence());
            //连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            //连接
            mqttClient.connect();
            //创建消息并发布消息
            MqttMessage msg = new MqttMessage("hello MQTT server".getBytes());
            /**
             *             QoS（Quality of Service，服务质量）级别定义了消息传递的可靠性
             *             QoS 0: 至多一次（At most once） 发送方发送消息后不要求确认，接收方不发送确认响应
             *             QoS 1: 至少一次（At least once）发送方在发送消息后要求接收方确认（PUBACK），如果没有收到确认，发送方会重发消息，直到收到确认为止
             *             QoS 2: 只有一次（Exactly once） 消息传递过程采用四步握手（PUBLISH、PUBREC、PUBREL、PUBCOMP），确保消息仅被接收一次。这种方式提供最高的传递保证，适用于要求严格不重复消息传递的场景。
             */
            msg.setQos(0);
            //客户端发布消息到主题上
            mqttClient.publish(topic, msg);
            System.out.println("Message published");
            System.out.println("topic: " + topic);
            System.out.println("message content: " + content);
            //断连
            mqttClient.disconnect();
            //关闭客户端
            mqttClient.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
