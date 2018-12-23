package Client.view;

import Client.controller.Controller;
import Client.net.OutputHandler;
import java.util.Scanner;

public class View {
    
    private final Controller controller = new Controller();
    private String username;
    private boolean chatting = false;
  
    public void startUp(){
        InputReader inputReader = new InputReader();
        inputReader.start();
    }
    
   private class InputReader extends Thread{
        public void run(){
            Scanner scan = new Scanner(System.in);
            System.out.println("Welcome to the chat application! Please enter your username");
            username = scan.nextLine();
            System.out.println("Nice to see you "+username+", to exit program type: QUIT, to join chat type JOIN");
            while(true){
                try{
                    String input = scan.nextLine();
                    if(input.equals("JOIN")){
                        System.out.println("joining chat... to leave the chat type: LEAVE_CHAT");
                        controller.joinChat(new ConsoleOutput(), username);
                        String message;
                        while (true) {
                            if(chatting){
                                if ((message = scan.nextLine()) != null) {
                                    if(message.equals("LEAVE_CHAT")){
                                       try{
                                            controller.leaveChat();
                                        }catch(Exception e){
                                            e.getMessage();
                                            System.err.println("force qutting...");
                                            System.exit(1);
                                        }
                                        System.out.println("You left the chat..., to join type: JOIN, to exit program type: QUIT");                                 
                                        controller.sendMessage("User "+username+" disconnected from chat");
                                        chatting = false;
                                        break;
                                    }
                                    controller.sendMessage(username+": "+message);
                                }
                            }
                        }
                    }else if(input.equals("QUIT")){
                        System.out.print("Quting program");
                        System.exit(1);
                    }else{
                        System.err.println("Invalid input!");
                    }
                }catch (Exception e){
                    if(e.getMessage().contains("failed to leave chat")){
                        System.err.println(e.getMessage());
                    }else{
                        System.err.println("Failed to establish connection with remote server. Try again with JOIN or exit program with QUIT");
                    }
                    continue;
                }
            }
        }
    }
     
     private class ConsoleOutput implements OutputHandler{
         
        @Override
        public void handleMessage(String msg){
               System.out.println(msg);
        }
        
        @Override
        public void reportMessageSendingFailure(String report){
            System.err.println(report);
        }
        
        @Override
        public void handlePrivateMessage(String msg){
            if(msg.equals("connectionOK")){
                chatting = true;
            }
        }
    }
}
