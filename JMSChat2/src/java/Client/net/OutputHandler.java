package Client.net;

public interface OutputHandler {
   
    void handleMessage(String message);
    
    void reportMessageSendingFailure(String report);
    
    void handlePrivateMessage(String message);
}