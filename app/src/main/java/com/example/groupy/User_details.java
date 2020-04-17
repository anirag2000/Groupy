package com.example.groupy;

public class User_details {
    String name;
    String date;
    String email;
    String group_id;
    String photourl;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }




    public User_details(String name_string, String date_string, String email_string, String group_id_string, String photourl,String uid) {
        this.name = name_string;
        this.date = date_string;
        this.email = email_string;
        this.group_id = group_id_string;
        this.photourl = photourl;
        this.uid=uid;
    }

    User_details(){

    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

}
