package Client.net;

import java.util.LinkedList;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
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
                        /*
                        while(true){
                            LinkedList<String> mClearQ2 = jmsConsumerQ2.receiveBodyNoWait(LinkedList.class);
                            if(mClearQ2 == null){
                                break;
                            }
                        }
                        */
                        //System.out.println("Spinning to flush queue3");
                        while(true){
                            String mClear = jmsConsumerQ3.receiveBodyNoWait(String.class);
                            if(mClear == null){
                                break;
                            }
                        }
                        //System.out.println("done flusing queue3");
                        //System.out.println("waiting for done");
                        String message = jmsConsumerQ3.receiveBody(String.class);
                        //System.out.println("received msg: "+message+ " from queue3");
                        if(message.equals("done")){
                            //System.out.println("Starting to collect data....");
                            LinkedList<String> listMsg = jmsConsumerQ2.receiveBody(LinkedList.class, 10000);
                           if(listMsg != null){
                                while(!listMsg.isEmpty()){
                                    String msgToSend = listMsg.getFirst();
                                    listMsg.removeFirst();
                                    outputHandler.handleMessage(msgToSend);
                                }
                            }
                        }
                        //System.out.println("continuing to live...");
                       //jmsConsumerQ2.close(); 
                        //jmsConsumerQ3.close();
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

