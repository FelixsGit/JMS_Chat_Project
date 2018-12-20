package Client.net;

import java.util.Enumeration;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Topic;

public class ChatConnection {
   
    
    public static ConnectionFactory connectionFactory;
    public static Queue queue;
    public static Queue queue2;
    public static Queue queue3;
    public static Topic topic;
    
    /**
     * This method is responsible for starting the listener and joining the chat
     * @param outputHandler observer interface to send chat messages to queue
     */
    public void joinChat(){
         JMSContext jmsContext = connectionFactory.createContext();
         JMSProducer jmsProducer = jmsContext.createProducer();
         jmsProducer.send(queue, "new");   
    }
    
    public void sendMessage(String message){
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();  
        jmsProducer.send(topic, message);
    }
    
    public void startListener(OutputHandler outputHandler){
        Listener listener = new Listener(outputHandler, this);
        listener.start();
    }
    
    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener extends Thread{

        private final OutputHandler outputHandler;
        private ChatConnection chatConnection;
        
        public Listener(OutputHandler outputHandler, ChatConnection chatConnection){
            this.outputHandler = outputHandler;
            this.chatConnection = chatConnection;
        }

        public void run(){
                 JMSContext jmsContext = connectionFactory.createContext();        
                 //Listening on queue
                 try{
                        JMSConsumer jmsConsumerQ3 = jmsContext.createConsumer(queue3); 
                        JMSConsumer jmsConsumerQ2 = jmsContext.createConsumer(queue2); 
                        //System.out.println("waiting on done");
                        String message = jmsConsumerQ3.receiveBody(String.class);
                       // System.out.println("received done message");
                        if(message.equals("done")){
                            for(;;){
                                //System.out.println("taking msg from queue2");
                                String msg = jmsConsumerQ2.receiveBodyNoWait(String.class);
                                //System.out.println("received msg: "+msg);
                                if(msg == null){
                                    //System.out.println("done collecting chat history");
                                    break;
                                }
                                outputHandler.handleMessage(msg);
                             }
                         } 
                     }catch (Exception e){
                     e.printStackTrace();
                    }
                
                 //chatConnection.sendMessage("new user joined chat");
                 outputHandler.handleMessage("live ->");
                 
                 //Listening on topic
                 try{
                     JMSConsumer jmsConsumerT = jmsContext.createConsumer(topic);
                     for(;;) {
                         String message =  jmsConsumerT.receiveBody(String.class);
                         outputHandler.handleMessage(message);
                     }
                 }catch (Exception e){
                     e.printStackTrace();
                }
            }
       }
   }

