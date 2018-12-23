package Client.net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.Topic;

public class ChatConnection {
   
    
    public static ConnectionFactory connectionFactory;
    public static Queue queue;
    public static Queue queue3;
    public static Topic topic;
    public static Topic topic2;
    
    /**
     * This method is responsible for starting the listener and joining the chat
     * @param outputHandler observer interface to send chat messages to queue
     */
    public void joinChat(){
         JMSContext jmsContext = connectionFactory.createContext();
         JMSProducer jmsProducer = jmsContext.createProducer();
         jmsProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
         jmsProducer.setPriority(9);
         jmsProducer.send(queue, "new");   
    }
    
    public void sendMessage(String message){
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();  
        jmsProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        jmsProducer.setPriority(9);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        jmsProducer.send(topic, timeStamp+"-->"+message);
    }
    
    public void startListener(OutputHandler outputHandler, String username){
        Listener listener = new Listener(outputHandler, this, username);
        listener.start();
    }
    
    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener extends Thread{

        private final OutputHandler outputHandler;
        private ChatConnection chatConnection;
        private final String username;
        
        public Listener(OutputHandler outputHandler, ChatConnection chatConnection, String username){
            this.outputHandler = outputHandler;
            this.chatConnection = chatConnection;
            this.username = username;
        }

        public void run(){
                 JMSContext jmsContext = connectionFactory.createContext();
                 //Listening on queue
                 try{                
                        JMSConsumer jmsConsumerQ3 = jmsContext.createConsumer(queue3);                     
                        JMSConsumer jmsConsumerT2 = jmsContext.createConsumer(topic2);
                        String message = jmsConsumerQ3.receiveBody(String.class);
                        //System.out.println("received msg: "+message+ " from queue3");
                        if(message.equals("done")){
                            LinkedList<String> listMsg = jmsConsumerT2.receiveBody(LinkedList.class, 2000);
                           if(listMsg != null){
                                while(!listMsg.isEmpty()){
                                    String msgToSend = listMsg.getFirst();
                                    listMsg.removeFirst();
                                    outputHandler.handleMessage(msgToSend);
                                }
                            }
                        }
                        //System.out.println("continuing to live...");
                       jmsConsumerQ3.close();
                 }catch (Exception e){
                     e.printStackTrace();
                     }
                
                 chatConnection.sendMessage("User "+username+" joined chat");
                 outputHandler.handleMessage("Live ->");
                 outputHandler.handlePrivateMessage("joined");
                 
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

