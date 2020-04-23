package com.example.groupy.Service;

public class Data {

    private String RUser;
    private int icon;
    private String body;
    private String title;
    private String sentBy;


    public Data(String RUser, int icon, String body, String title, String sentBy) {
        this.RUser = RUser;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sentBy = sentBy;
    }

    public Data() {
    }

    public String getRUser() {
        return RUser;
    }

    public void setRUser(String RUser) {
        this.RUser = RUser;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}

