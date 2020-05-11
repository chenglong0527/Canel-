package com.chenglong.canel;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;


public class DelayMessageConsumer {

    public static void main(java.lang.String args[]) throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delayConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("CreateOrderInformTopic", "user");
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                if (msgs != null) {
                    for (MessageExt message : msgs) {
                        System.out.println(message.getMsgId() + "   " + (System.currentTimeMillis() - message.getStoreTimestamp())
                        +"    "+ (System.currentTimeMillis() - message.getBornTimestamp()));
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.start();
        while (true){
            Thread.sleep(1000);
        }
    }

}
/*public class DelayMessageConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delayConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("CreateOrderInformTopic","user");
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                if(msgs!=null){
                    for (MessageExt message:msgs){
                        System.out.println(message.getMsgId()+"   "+(System.currentTimeMillis()-message.getStoreTimestamp()));
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }
}*/
