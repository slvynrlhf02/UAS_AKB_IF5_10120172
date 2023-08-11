package com.UAS_AKB_IF5_10120172.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Note implements Serializable {

    @Exclude
    String id; // This field will be excluded from Firebase Database
    String title;
    String category;
    String desc;
    String date;

    public Note() {
        // Default constructor required for Firebase Database
    }

    public Note(String id, String title, String category, String desc, String date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.desc = desc;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF5