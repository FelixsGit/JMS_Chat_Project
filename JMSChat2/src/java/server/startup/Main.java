package server.startup;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import server.net.ChatRecorder;
import server.net.ClientHandler;

public class Main {
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jsm/chatQueue")
    private static Queue clientQueue;
    
    @Resource(mappedName = "jms/sendQueue")
    private static Queue msgQueue;
    
    @Resource(mappedName = "jms/confirmQueue")
    private static Queue confirmQueue;
    
    @Resource(mappedName = "jms/chatTopicConnectionFactory")
    private static TopicConnectionFactory topicConnectionFactory;
    
    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;
    
    public static void main(String[] args) throws JMSException, InterruptedException {
        ClientHandler clientHandler = new ClientHandler(connectionFactory, clientQueue, msgQueue, confirmQueue);
        new ChatRecorder(clientHandler, topicConnectionFactory, topic);
        clientHandler.startClientHandler();
    }
}
