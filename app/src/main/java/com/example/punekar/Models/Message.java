package com.example.punekar.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

     String sender;
     String recipient;
     String message;
     String senderName;
     String recipientName;

    @ServerTimestamp
    private Date date;

    public Message() {
    }

    public Message(String sender, String recipient, String message, String senderName, String recipientName) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.senderName = senderName;
        this.recipientName = recipientName;
    }



    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
