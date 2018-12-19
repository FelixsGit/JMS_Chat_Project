package chat.sender;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

public class Sender {

    @Resource(mappedName = "jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jsm/chatQueue")
    private static Queue queue;
    
   static  Scanner scan = new Scanner(System.in);
   
   
    public static void main(String[] args) {
        
        JMSContext jmsContext = connectionFactory.createContext();
        JMSProducer jmsProducer = jmsContext.createProducer();
           
        System.out.println("Sending message to JMS - ");
        System.out.println("Welcome to chat, please enter username");
        String username = scan.nextLine();
        
       while(true){
           String message = scan.nextLine();
           System.out.print("say: ");
           if(message.equals("break")){
               break;
           }
           jmsProducer.send(queue, username+": "+message);
       }
        jmsProducer.send(queue,"User "+ username+" disconnected from chat");
        System.out.println("Message Send Successfully");
        
    }
    
}
