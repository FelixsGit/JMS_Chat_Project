package Client.controller;

import Client.net.ChatConnection;
import Client.net.OutputHandler;
import java.util.concurrent.CompletableFuture;

public class Controller {
    
        private ChatConnection chatConnection = new ChatConnection();

        public void joinChat(){
            CompletableFuture.runAsync(() -> {
                 chatConnection.joinChat();
       });
        }
        
        public void startListener(OutputHandler outputHandler, String username){
             CompletableFuture.runAsync(() -> {
                 chatConnection.startListener(outputHandler, username);
         });
        }
        
        
        public void sendMessage(String message){
           CompletableFuture.runAsync(() -> {
                 chatConnection.sendMessage(message);
         });
}

}
        
