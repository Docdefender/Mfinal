package com.example.mfinal.models;

public class Labels {
    String label ;
    String description;

    public String getLabel() {
        return label;
    }

    public Labels() {
    }

    public Labels(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
