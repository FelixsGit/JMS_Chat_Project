package Client.startup;

import Client.net.ChatConnection;
import Client.view.View;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
public class Main {
    
    /**
     * TODO, figure out a way to connect to the Glassfish JMS resources from within the net layer, 
     * This is a bad quick fix that leif will not approve off.
     * */
    
    
    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jsm/chatQueue")
    private static Queue queue;
    
    @Resource(mappedName = "jsm/chatQueue3")
    private static Queue queue3;
    
    @Resource(mappedName = "jsm/chatTopic")
    private static Topic topic;
    
    @Resource(mappedName = "jsm/chatTopic2")
    private static Topic topic2;
    
    /**
     * Method is called when the client start the applications
     * @param args , currently to arguments
     */
    public static void main(String[] args){
        
        ChatConnection.connectionFactory = Main.connectionFactory;
        ChatConnection.queue = Main.queue;
        ChatConnection.topic = Main.topic;
        ChatConnection.topic2 = Main.topic2;
        ChatConnection.queue3 = Main.queue3;
        
        View view = new View();
        view.startUp();
    }
    
}
