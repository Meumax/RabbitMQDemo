package cn.klb.rabbitmq.consumer.headers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @Author: Konglibin
 * @Description:
 * @Date: Create in 2020/5/23 11:09
 * @Modified By:
 */
public class ConsumerHeadersSMS {
    // 队列名称
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    // 交换机名称
    private static final String EXCHANGE_HEADERS_INFORM = "exchange_headers_inform";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        // 创建连接
        Connection connection = factory.newConnection();

        // 创建通道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_HEADERS_INFORM, BuiltinExchangeType.HEADERS);

        // 声明队列
        channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

        // 交换机和队列绑定
        Map<String,Object> headers_sms = new Hashtable<String,Object>();
        headers_sms.put("inform_sms","sms");
        channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_HEADERS_INFORM, "",headers_sms);

        // 定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println(msg);
            }
        };

        // 监听队列
        channel.basicConsume(QUEUE_INFORM_SMS, true, consumer);
    }
}
