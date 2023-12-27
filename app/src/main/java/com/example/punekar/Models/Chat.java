package com.example.punekar.Models;

public class Chat {

    String senderName;
    String recipientName;

    public Chat() {
    }

    public Chat(String senderName, String recipientName) {
        this.senderName = senderName;
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
