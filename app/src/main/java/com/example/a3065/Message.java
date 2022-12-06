package com.example.a3065;

public class Message {
    private String sender,text,timestamp,receiver;

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public Message(String sender, String receiver, String text, String timestamp){
        this.sender=sender;
        this.text=text;
        this.receiver=receiver;
        this.timestamp=timestamp;

    }
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
