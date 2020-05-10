package com.chenglong.canel;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class RocketMQConsumer {

    public static void main(String args[]) throws MQClientException, InterruptedException {

        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_consumer1");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("example", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
                                             @Override
                                             public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext context) {
                                                 context.setAutoCommit(true);
                                                 try {

                                                     //对有序的消息进行处理
                                                     if (list != null && !list.isEmpty()) {
                                                         for (MessageExt message : list) {
                                                             byte[] body = message.getBody();
                                                             String s = new String(body);
                                                             System.out.println(s);

//                                                             int i=1/0;
                                                             System.out.println("处理了");
//                                                             System.out.println("备份数据库入库");
                                                             SQLOperation.operateSQL(s);
                                                         }
                                                         return ConsumeOrderlyStatus.SUCCESS;
                                                     }
                                                 } catch (Exception e) {
                                                     //如果消息处理有问题
                                                     //返回一个状态，让他暂停一会再处理
                                                     System.out.println("啦啦啦处理失败了等会再来处理哦！！！");
                                                     return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                                                 }
                                                 return null;
                                             }
                                         }
        );
        consumer.start();
        while (true) {
            Thread.sleep(1000);
        }
    }

}