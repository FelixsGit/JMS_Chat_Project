package server.net;

import java.util.List;
import server.model.Conversation;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

public class ClientHandler {
    
    private Conversation conversation = new Conversation();
    JMSContext jmsContext;
    JMSConsumer jmsPrimaryConsumer;
    JMSConsumer jmsSecondaryConsumer;
    JMSProducer jmsProducer;
    Queue clientQueue;
    
    public ClientHandler(ConnectionFactory connectionFactory, Queue clientQueue, Queue msgQueue, Queue confirmQueue) {
        jmsContext = connectionFactory.createContext();
        jmsPrimaryConsumer = jmsContext.createConsumer(clientQueue);
        jmsProducer = jmsContext.createProducer();
        this.clientQueue = clientQueue;
    }
    
    public void storeMsg(String msg) {
        conversation.storeMsg(msg);
    }
    
    public void startClientHandler() throws InterruptedException, JMSException {
        listenForNewUsers();
    }
    
    private void listenForNewUsers() throws InterruptedException, JMSException {
        System.out.println("Starting to handle clients!");
        while (true) {
            Message msg = jmsPrimaryConsumer.receive();
            if (msg.getBody(String.class).equals("###")) {
                Queue msgQueue = (Queue) msg.getJMSReplyTo();
                System.out.println("Message caught " + msg.getBody(String.class));
                List<String> messages = conversation.getMessages();
                for (int i = 0; i < messages.size(); i++) {
                    System.out.println("Sending: " + messages.get(i));
                    jmsProducer.send((Destination) msgQueue, messages.get(i));
                }
                jmsProducer.send((Destination) msgQueue, "done");
                System.out.println("Sending a break call****************");
            }
            
        }
    }
    
}
