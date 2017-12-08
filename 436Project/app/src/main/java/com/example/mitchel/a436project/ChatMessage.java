package com.example.mitchel.a436project;


import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageType;
    private String senderAddress;
    private boolean clicked;

    public ChatMessage(String messageText, String messageUser, String messageType) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageType = messageType;
        clicked = false;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void click(){ clicked = true; }

    public boolean isClicked(){return clicked;}

}
