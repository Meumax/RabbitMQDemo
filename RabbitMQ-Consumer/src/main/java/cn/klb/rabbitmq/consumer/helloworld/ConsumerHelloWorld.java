package cn.klb.rabbitmq.consumer.helloworld;

import java.io.IOException;

import com.rabbitmq.client.*;

/**
 * @Author: Konglibin
 * @Description:
 * @Date: Create in 2020/5/19 18:58
 * @Modified By:
 */
public class ConsumerHelloWorld {

    private static final String QUEUE = "queueOfKlb";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        // 1.获取连接
        Connection connection = factory.newConnection();
        // 2.创建通道
        Channel channel = connection.createChannel();
        // 3.声明队列
        channel.queueDeclare(QUEUE, true, false, false, null);

        // 定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("收到的消息是：" + msg);
            }
        };

        channel.basicConsume(QUEUE, true, consumer);

    }
}
