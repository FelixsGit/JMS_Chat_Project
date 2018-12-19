package Client.startup;

import Client.view.View;

public class Main {
       
    /**
     * Method is called when the client start the applications
     * @param args , currently to arguments
     */
    public static void Main(String[] args){
        View view = new View();
        view.startUp();
    }
    
}
