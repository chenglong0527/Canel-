package com.chenglong.canel;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class RocketMQConsumer {

    public static void main(String args[]) throws MQClientException, InterruptedException {

        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_consumer1");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("example", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                System.out.println(list);
                if(list!=null&&!list.isEmpty()){
                    for (MessageExt message:list){
                        byte[] body = message.getBody();
                        String s = new String(body);
                        System.out.println(s);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        while (true) {
            Thread.sleep(1000);
        }
    }

}