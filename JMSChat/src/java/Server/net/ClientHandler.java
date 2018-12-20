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
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

public class ClientHandler extends Thread{
    
    public static ConnectionFactory connectionFactory;
    
    public static Queue queue;
    
    public static Queue queue2;
    
    public static Queue queue3;
    
    JMSContext jmsContext = connectionFactory.createContext();
    JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
    JMSProducer jmsProducer = jmsContext.createProducer();
    
    private Conversation conversation;
    
    public ClientHandler(Conversation conversation){
        this.conversation = conversation;
    }
 
    public void run() {
        System.out.println("Server listening on new clients...");
        while (true) {
            String message = jmsConsumer.receiveBody(String.class);
            if (message.equals("new")) {
                System.out.println("Server spotted new clinet!");
                LinkedList<String> chatHistory = conversation.getMessages();
                while(!chatHistory.isEmpty()){
                    String msg = chatHistory.getFirst();
                    chatHistory.removeFirst();
                    System.out.println("Server sending message: "+msg);
                    jmsProducer.send(queue2, msg);
                }
                System.out.println("Server sending message: done");
                jmsProducer.send(queue3,"done");
            }
            
        }
        
    }
    
}