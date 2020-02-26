package com.hzj;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import java.io.IOException;

public class Demo {
    public static final String ACTIVEMQ_URL = "tcp://120.25.164.172:61616";
    public static final String LOCALHOST_URL = "tcp://localhost:61616";
    public static final String TOPIC_NAME = "TOPIC";

    public static void main(String[] args) throws Exception {
//        consumer();
        producer();
//        embedBroker();

    }
    public static void embedBroker() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }

    public static void producer() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(LOCALHOST_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageProducer producer = session.createProducer(topic);
        connection.start();
        for (int i = 1; i < 4; i++) {
            TextMessage textMessage = session.createTextMessage("ttttopic");
            producer.send(textMessage);
        }
        producer.close();
        session.close();
        connection.close();
    }

    public static void consumer() throws JMSException, IOException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(LOCALHOST_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageConsumer consumer = session.createConsumer(topic);
        connection.start();
        Message message = consumer.receive();
        while(message != null && message instanceof TextMessage){
            System.out.println("接收到消息："+((TextMessage) message).getText());
            message = consumer.receive(4000L);
        }
        consumer.close();
        session.close();
        connection.close();
    }
}
