package com.example.learning.fragments;

public class FolderItem {
    public String title;
    public String description;


    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FolderItem(String title, String description) {
        this.description = description;
        this.title = title;

    }
}
