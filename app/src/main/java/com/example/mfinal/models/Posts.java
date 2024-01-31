package com.example.mfinal.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Posts {
    String url;
    String uid;
    Timestamp creationTimestamp;
    ArrayList<String> labels;

    public Posts(String url, String uid, Timestamp creationTimestamp, ArrayList<String> labels) {
        this.url = url;
        this.uid = uid;
        this.creationTimestamp = creationTimestamp;
        this.labels = labels;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public Posts() {
    }
}