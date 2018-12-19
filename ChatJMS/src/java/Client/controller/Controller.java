package Client.controller;

import Client.net.ChatConnection;
import Client.net.OutputHandler;
import java.util.concurrent.CompletableFuture;

public class Controller {
    
        private final ChatConnection chatConnection = new ChatConnection();
        
   
        public void joinChat(OutputHandler outputHandler){
            CompletableFuture.runAsync(() -> {
                 chatConnection.joinChat(outputHandler);
        });
           
             
        }
        public void sendMessage(String message){
            CompletableFuture.runAsync(() -> {
                 chatConnection.sendMessage(message);
        });
        }
        
}
