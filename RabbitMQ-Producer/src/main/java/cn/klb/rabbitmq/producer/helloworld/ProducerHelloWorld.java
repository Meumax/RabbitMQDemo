package cn.klb.rabbitmq.producer.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: Konglibin
 * @Description:
 * @Date: Create in 2020/5/19 17:49
 * @Modified By:
 */
public class ProducerHelloWorld {

    private static final String QUEUE = "queueOfKlb";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        // 1.获取连接
        Connection connection = factory.newConnection();
        // 2.创建通道
        Channel channel = connection.createChannel();
        // 3.声明队列
        channel.queueDeclare(QUEUE, true, false, false, null);

        String msg = "helloworld立斌" + System.currentTimeMillis();

        // 4.发布消息
        channel.basicPublish("", QUEUE, null, msg.getBytes());
        System.out.println("发送的消息是：" + msg);
    }
}
