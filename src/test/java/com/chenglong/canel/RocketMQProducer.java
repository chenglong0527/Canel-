package com.chenglong.canel;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.platform.commons.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class RocketMQProducer {
    public static final DefaultMQProducer producer = new DefaultMQProducer("test_producer");

    static {
        producer.setNamesrvAddr("127.0.0.1:9876");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static void sendSQL(String sql, String orderId) {
        if (StringUtils.isBlank(sql)) {
            return;
        }
        Message message = null;
        try {
            message = new Message("example", "TagA", sql.getBytes(RemotingHelper.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendResult send = null;
        try {
            send = producer.send(message,
                    new MessageQueueSelector() {
                        @Override
                        public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                            String orderId = arg.toString();//每一条记录都会有固定的orderId
                            int hashCode = orderId.hashCode();
                            int index = hashCode % mqs.size();//每条记录的操作都会进入同一个MessageQueue
//                            System.out.println("进入了MessageQueue：" + index);
                            return mqs.get(index);
                        }
                    }, orderId);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(send);
    }
}
