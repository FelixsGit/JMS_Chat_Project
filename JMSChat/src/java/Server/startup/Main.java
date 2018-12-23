/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.startup;

import Server.model.Conversation;
import Server.net.ChatRecorder;
import Server.net.ClientHandler;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 *
 * @author adria
 */
public class Main {
    
    @Resource(name="jms/chatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(mappedName="jsm/chatQueue")
    private static Queue queue;
    
    @Resource(mappedName="jsm/chatQueue2")
    private static Queue queue2;
    
    @Resource(mappedName="jsm/chatQueue3")
    private static Queue queue3;
    
    @Resource(mappedName="jsm/chatTopic")
    private static Topic topic;
    
    @Resource(mappedName="jsm/chatTopic2")
    private static Topic topic2;
    
    public static void main(String[] arg) {
        
        ChatRecorder.connectionFactory = connectionFactory;
        ChatRecorder.topic = topic;
       
        ClientHandler.connectionFactory = connectionFactory;
        ClientHandler.queue = queue;
        ClientHandler.queue2 = queue2;
        ClientHandler.queue3 = queue3;
        ClientHandler.topic2 = topic2;
       
        Conversation conversation = new Conversation();
        ClientHandler clientHandler = new ClientHandler(conversation);
        clientHandler.start();
        ChatRecorder chatRecorder = new ChatRecorder(conversation);
        chatRecorder.start();
    }
}