package Client.net;

import common.ConnectionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

public class ChatConnection {

    public static ConnectionFactory connectionFactory;
    public static TopicConnectionFactory tcf;
    public static Queue clientQueue;
    public static Topic topic;
    private OutputHandler outputHandler;
    private Listener listener;

    public void joinChat(OutputHandler outputHandler, String username) throws ConnectionException{
            this.outputHandler = outputHandler;
            ChatJoiner chatJoiner = new ChatJoiner(outputHandler, username);
            ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
            try{
                threadExecutor.submit(chatJoiner).get();
            }catch(Exception e){
                    throw new ConnectionException("Server connection failed");
            }
    }

    public void sendMessage(String message){
        try{
            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer jmsProducer = jmsContext.createProducer();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            jmsProducer.send(topic, timeStamp+"->"+message);
        }catch(Exception e){
            outputHandler.reportMessageSendingFailure("Failed to send message");
        }
    }
    
    public void leaveChat() throws Exception{
        listener.disconnect();
    }

    private void startListener(OutputHandler outputHandler) throws Exception {
        try{
            listener = new Listener(outputHandler);
        }catch(Exception e){
            throw new Exception("Failed to leave chat");
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private class Listener implements MessageListener{

        private final OutputHandler outputHandler;
        private TopicConnection topicConnection;

        public Listener(OutputHandler outputHandler) throws Exception {
            this.outputHandler = outputHandler;
            try {
                topicConnection = tcf.createTopicConnection();
                TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
                topicSession.createSubscriber(topic).setMessageListener(this);
                topicConnection.start();
            } catch (Exception ex) {
                throw new Exception("Listener failure");
            }          
        }
        
        public void disconnect() throws Exception{
            topicConnection.close();
        }

        @Override
        public void onMessage(Message message) {
            try {
                outputHandler.handleMessage(message.getBody(String.class));
            } catch (JMSException ex) {
                Logger.getLogger(ChatConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      private class ChatJoiner implements Callable{
        
        private OutputHandler outputHandler;
        private String username;
        
        public ChatJoiner(OutputHandler outputHandler, String username){
            this.outputHandler = outputHandler;
            this.username = username;
        }
        
        @Override
        public Integer call() throws Exception{
            try{
                JMSContext jmsContext = connectionFactory.createContext();
                TemporaryQueue queue = jmsContext.createTemporaryQueue();
                JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(queue);
                JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
                jmsProducer.send((Destination) clientQueue, "###");
                while (true) {
                    String msg = jmsConsumer.receiveBody(String.class, 5000);
                    if (msg == null) {
                        outputHandler.handleMessage("FAILED TO RETRIEVE CHAT HISTORY");
                        break;
                    }else if(msg.equals("done")){
                        sendMessage("User "+username+" joined the chat");
                        break;
                    }else{
                        outputHandler.handleMessage(msg);
                    }
                }
                outputHandler.handleMessage("Live-->");
                outputHandler.handleConnectionMessage("connectionOK");
                startListener(outputHandler);
            }catch(Exception e){
                throw new Exception(e.getMessage());
            }
            return null;
        }
    }
}
