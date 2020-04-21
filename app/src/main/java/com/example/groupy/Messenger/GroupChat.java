package com.example.groupy.Messenger;

public class GroupChat {

    String group;
    String sender;
    String message;
    String sentByName;
    String date;
    String senderphoto;
    public GroupChat(String group, String sender,String senderphoto, String message, String sentByName, String date) {
        this.group = group;
        this.sender = sender;
        this.senderphoto=senderphoto;
        this.message = message;
        this.sentByName = sentByName;
        this.date = date;
    }
    GroupChat(){

    }

    public String getSenderphoto() {
        return senderphoto;
    }

    public void setSenderphoto(String senderphoto) {
        this.senderphoto = senderphoto;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public String getSentByName() {
        return sentByName;
    }

    public void setSentByName(String sentByName) {
        this.sentByName = sentByName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}