/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.net;

import Server.model.Conversation;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class ChatRecorder extends Thread{
    
    
    public static ConnectionFactory connectionFactory;
    
    public static Topic topic;
    
    JMSContext jmsContext = connectionFactory.createContext();
    
    private Conversation conversation;
    
    
    public ChatRecorder(Conversation conversation){
        this.conversation = conversation;
    }
                
    public void run(){
          //Listening on topic
                 try{
                      System.out.println("Server listening on chat topic messages");
                     JMSConsumer jmsConsumerT = jmsContext.createConsumer(topic);
                     for(;;) {
                         String message =  jmsConsumerT.receiveBody(String.class);
                         conversation.storeMsg(message);
                         System.out.println("message: --"+message+"-- added to history");
                     }
                 }catch (Exception e){
                     e.printStackTrace();
                }
    }
    
}