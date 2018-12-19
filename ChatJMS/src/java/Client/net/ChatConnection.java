package Client.net;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.Topic;

public class ChatConnection {
   
    public static ConnectionFactory connectionFactory;
    public static Queue queue;
    public static Topic topic;
    
    /**
     * This method is responsible for starting the listener and joining the chat
     * @param outputHandler observer interface to send chat messages to queue
     */
    public void joinChat(OutputHandler outputHandler){
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
     
        startListener(outputHandler);
        jmsProducer.send(queue, "new");    
    }
    
    public void sendMessage(String message){
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();  
        
        jmsProducer.send(topic, message);
    }
    
    private void startListener(OutputHandler outputHandler){
        Listener listener = new Listener(outputHandler);
        listener.start();
    }
    
    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener extends Thread{

        private final OutputHandler outputHandler;
       

        public Listener(OutputHandler outputHandler){
            this.outputHandler = outputHandler;
        }

        public void run(){
            
                 JMSContext jmsContext = connectionFactory.createContext();
                 //Listening on queue
                 try{
                      JMSConsumer jmsConsumerQ = jmsContext.createConsumer(queue);
                     for(;;) {
                         String message = jmsConsumerQ.receiveBody(String.class);
                         if(message.equals("###")){
                             break;
                         }                        
                         outputHandler.handleMessage(message);
                     }
                 }catch (Exception e){
                     e.printStackTrace();
                 }
                 
                 
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

