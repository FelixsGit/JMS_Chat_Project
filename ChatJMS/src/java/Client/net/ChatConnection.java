package Client.net;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

public class ChatConnection {
    
    @Resource(mappedName = "jms/chatConnectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jsm/chatQueue")
     Queue queue;
 
    /**
     * This method is responsible for starting the listener and joining the chat
     * @param outputHandler observer interface to send chat messages to queue
     */
    public void joinChat(OutputHandler outputHandler) {
        startListener(outputHandler);
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
        jmsProducer.send(queue, "new");
    }
   
    public void sendMessage(String message){
        //TODO functionality to send message to topic
    }
    
    private void startListener(OutputHandler outputHandler){
        Listener listener = new Listener(outputHandler);
        listener.start();
    }
    
    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener extends Thread{

        private final OutputHandler outputHandler;
        JMSContext jmsContext = connectionFactory.createContext();
        JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);

        public Listener(OutputHandler outputHandler){
            this.outputHandler = outputHandler;
        }

        public void run(){
            
            //Listening on queue
             try{
                for(;;) {
                    //outputHandler.handleMessage((MessageDTO)fromServer.readObject());
                    String message = jmsConsumer.receiveBody(String.class);
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
                for(;;) {
                    //outputHandler.handleMessage((MessageDTO)fromServer.readObject());
                }
            }catch (Exception e){
               e.printStackTrace();
            }
        }
    }
}
