package Client.net;

public interface OutputHandler {
   
    void handleMessage(String message);
    
    void handlePrivateMessage(String message);
}
