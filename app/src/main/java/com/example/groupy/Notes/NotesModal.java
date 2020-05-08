package com.example.groupy.Notes;


//Notes Class

public class NotesModal {
    public String title;

    public String description;
    public String uid;
 NotesModal(){}
    public NotesModal(String title, String description, String uid) {
        this.title = title;
        this.description = description;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
