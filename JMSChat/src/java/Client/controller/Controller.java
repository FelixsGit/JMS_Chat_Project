package Client.controller;

import Client.net.ChatConnection;
import Client.net.OutputHandler;
import java.util.concurrent.CompletableFuture;

public class Controller {
    
        private final ChatConnection chatConnection = new ChatConnection();

        public void joinChat(){
            CompletableFuture.runAsync(() -> {
                 chatConnection.joinChat();
       });
        }
        
        public void startListener(OutputHandler outputHandler){
             CompletableFuture.runAsync(() -> {
                 chatConnection.startListener(outputHandler);
         });
        }
        
        
        public void sendMessage(String message){
           CompletableFuture.runAsync(() -> {
                 chatConnection.sendMessage(message);
         });
    }

}
        
