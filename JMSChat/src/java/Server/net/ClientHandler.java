/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.net;

import Server.model.Conversation;
import java.util.LinkedList;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.Topic;

public class ClientHandler extends Thread{
    
    public static ConnectionFactory connectionFactory;
    
    public static Topic topic2;
    
    public static Queue queue;
    
    public static Queue queue2;
    
    public static Queue queue3;
    
    JMSContext jmsContext = connectionFactory.createContext();
    JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
    JMSConsumer jmsConsumerQ2 = jmsContext.createConsumer(queue2);
    JMSProducer jmsProducer = jmsContext.createProducer();
    
    private Conversation conversation;
    
    public ClientHandler(Conversation conversation){
        this.conversation = conversation;
    }
 
    public void run() {
     
        System.out.println("Server listening on new clients...");
        while (true) {
            System.out.println("waiting on a new client");
            String message = jmsConsumer.receiveBody(String.class);
            if (message.equals("new")) {
                System.out.println("Server spotted new client");
                LinkedList<String> msg = conversation.getMessages();
                System.out.println("sending data"+msg);
                jmsProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
                jmsProducer.setPriority(9);
                jmsProducer.send(topic2, msg);
                jmsProducer.send(queue3,"done");
            }         
        }
    }
}