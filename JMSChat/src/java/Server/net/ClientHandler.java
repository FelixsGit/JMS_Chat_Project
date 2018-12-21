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
    JMSConsumer jmsConsumerQ2 = jmsContext.createConsumer(queue2);
    JMSProducer jmsProducer = jmsContext.createProducer();
    
    private Conversation conversation;
    
    public ClientHandler(Conversation conversation){
        this.conversation = conversation;
    }
 
    public void run() {
        while(true){
            LinkedList<String> mClearQ2 = jmsConsumerQ2.receiveBodyNoWait(LinkedList.class);
            if(mClearQ2 == null){
            break;
            }
        }
        System.out.println("Server listening on new clients...");
        while (true) {
            System.out.println("Spining to flush queue1");
            while(true){
                String mClear = jmsConsumer.receiveBodyNoWait(String.class);
                if(mClear == null){
                    break;
                }
            }
            System.out.println("done flusing queue1");
            System.out.println("waiting on a new client");
            String message = jmsConsumer.receiveBody(String.class);
            if (message.equals("new")) {
                System.out.println("Server spotted new client");
                /*
                while(!chatHistory.isEmpty()){
                    String msg = chatHistory.getFirst();
                    chatHistory.removeFirst();
                    System.out.println("Server sending message: "+msg);
                    jmsProducer.send(queue2, msg);      
                }
                */
                //conversation.storeMsg("set");
                //conversation.removeMsg();
                LinkedList<String> msg = conversation.getMessages();
                if(!msg.isEmpty()){
                    System.out.println("sending data"+msg);
                    jmsProducer.send(queue2, msg);
                    jmsProducer.send(queue3,"done");
                }else{
                    System.out.println("Chat history is empty");
                     jmsProducer.send(queue3,"empty");
                }
                System.out.println("Done sending data");
            }         
        }
    }
}