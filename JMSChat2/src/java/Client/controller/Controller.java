package Client.controller;

import Client.net.ChatConnection;
import Client.net.OutputHandler;
import common.ConnectionException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    
        private final ChatConnection chatConnection = new ChatConnection();

        public void joinChat(OutputHandler outputHandler, String username) throws ConnectionException{          
            chatConnection.joinChat(outputHandler, username);
        }
        
        public void leaveChat() throws Exception{
               chatConnection.leaveChat();
        }
        
        public void sendMessage(String message){
           CompletableFuture.runAsync(() -> {
                chatConnection.sendMessage(message);
         });
    }

}
        
