package com.example.groupy.Messenger;

public class GroupChat {

    String group;
    String sender1;
    String message1;
    String sentByName;
    String date;
    String senderphoto;
    public GroupChat(String group, String sender1,String senderphoto, String message1, String sentByName, String date) {
        this.group = group;
        this.sender1= sender1;
        this.senderphoto=senderphoto;
        this.message1 = message1;
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
        return sender1;
    }

    public void setSender(String sender1) {
        this.sender1 = sender1;
    }

    public String getMessage() {
        return message1;
    }

    public void setMessage(String message1) {
        this.message1 = message1;
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