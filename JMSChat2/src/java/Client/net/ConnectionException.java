package client.net;

public class ConnectionException extends Exception{
    
    /**
     * Exception that is thrown to get more control, rather than to use JMSException
     * @param reason 
     */
    public ConnectionException(String reason){
        super(reason);
    }
}
