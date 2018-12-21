/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.model;

import java.util.LinkedList;

public class Conversation {
    LinkedList<String> conversation = new LinkedList<String>();
    
    
    public LinkedList<String> getMessages() {
        return conversation;
    }
    
    public void storeMsg(String msg) {
        conversation.addLast(msg);
    }
    
    public void removeMsg(){
        conversation.removeLast();
    }
}
