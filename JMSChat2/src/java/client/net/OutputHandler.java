package client.net;

public interface OutputHandler {
   
    void handleMessage(String message);
    
    void reportChatHistoryLoss(String message);
    
    void reportMessageSendingFailure(String report);
    
    void handleConnectionMessage(String message);
}
