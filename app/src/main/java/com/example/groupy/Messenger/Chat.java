package com.example.groupy.Messenger;

public class Chat {
    String reciever;
    String sender;
    String message;




    String date;

    public Chat(String reciever, String sender, String message,String date) {
        this.reciever = reciever;
        this.sender = sender;
        this.message = message;
        this.date=date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public Chat() {
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}