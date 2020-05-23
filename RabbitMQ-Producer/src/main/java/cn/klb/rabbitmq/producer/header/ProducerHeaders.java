package cn.klb.rabbitmq.producer.header;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Konglibin
 * @Description:
 * @Date: Create in 2020/5/23 14:13
 * @Modified By:
 */
public class ProducerHeaders {
    // 队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    // 交换机名称
    private static final String EXCHANGE_HEADERS_INFORM = "exchange_headers_inform";

    public static void main(String[] args) {

        ConnectionFactory factory = null;
        Connection connection = null;
        Channel channel = null;

        try {
            // 配置连接工厂
            factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");

            // 创建连接
            connection = factory.newConnection();

            // 创建与交换机的通道，每个通道代表一个绘画
            channel = connection.createChannel();

            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_HEADERS_INFORM, BuiltinExchangeType.HEADERS);

            // 声明队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            // 交换机和队列绑定
            Map<String,Object> headers_email = new Hashtable<String,Object>();
            headers_email.put("inform_email","email");
            Map<String,Object> headers_sms = new Hashtable<String,Object>();
            headers_sms.put("inform_sms","sms");
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_HEADERS_INFORM, "",headers_email);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_HEADERS_INFORM, "",headers_sms);

            // 定义header
            Map<String,Object> headers = new Hashtable<String,Object>();
            headers.put("inform_email","email");    // 匹配email通知消费者绑定的header
            //headers.put("inform_sms","sms");    // 匹配sms通知消费者绑定的header

            AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties.Builder();
            properties.headers(headers);

            // 发送消息
            for (int i = 0; i < 5; i++) {
                String msg = "inform to user" + i;
                // 向交换机发送msg
                channel.basicPublish(EXCHANGE_HEADERS_INFORM, "", properties.build(), msg.getBytes());
                System.out.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
