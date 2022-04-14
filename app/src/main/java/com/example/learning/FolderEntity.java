package com.example.learning;

public class FolderEntity {
    private String folderName;
    private String folderDescription;
    private int folderID;
    private String time;
    private int userId;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderDescription() {
        return folderDescription;
    }

    public void setFolderDescription(String folderDescription) {
        this.folderDescription = folderDescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public FolderEntity(String folderName, String folderDescription, int folderID, String time, int userId) {
        this.folderName = folderName;
        this.folderDescription = folderDescription;
        this.folderID = folderID;
        this.time = time;
        this.userId = userId;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
