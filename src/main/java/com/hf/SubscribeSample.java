package com.hf;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author hf
 * 创建一个订阅客户端类 SubscribeSample，该类将订阅主题 mqtt/test。
 * @date 2024/6/14
 */
public class SubscribeSample {

    public static void main(String[] args) {
        String broker = "tcp://broker.emqx.io:1883";
        String topic = "mqtt/test";
        String username = "emqx";
        String password = "public";
        String clientid = "subscribe_client";
        int qos = 0;
        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            // 设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失时的回调方法
                    System.out.println("Connection Lost " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //接受到消息时的回调方法
                    System.out.println("topic: " + topic);
                    System.out.println("messageId: "  + message.getId());
                    System.out.println("Qos mode: "+message.getQos());
                    byte[] payload = message.getPayload();
                    System.out.println(payload);
                    System.out.println("message content: " + new String(payload));

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //消息发送完成时候的回调
                    MqttMessage message = null;
                    try {
                        message = token.getMessage();
                        System.out.println("发送的主题: " + token.getTopics().toString());
                        System.out.println("发送的消息ID: "  + message.getId());
                        System.out.println("发送的消息的Qos模式: "+message.getQos());
                        System.out.println("发送出去的消息: "+new String(message.getPayload()));

                        System.out.println("发送结果" + (token.isComplete() ? "成功" :"失败"));
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
            });
            client.connect(options);
            client.subscribe(topic, qos);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
