package com.example.a3065.chat;

public class ChatList {



    public String getSender() {
        return sender;
    }



    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }


    public ChatList(String sender, String message, String date) {

        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    private String name,sender,message,date,time;

}
