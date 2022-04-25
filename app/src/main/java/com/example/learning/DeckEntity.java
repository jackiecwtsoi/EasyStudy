package com.example.learning;

import java.io.Serializable;

public class DeckEntity implements Serializable {
    private String deckName;
    private int completion;
    private String deckDescription;
    private String time;
    private int deckID;
    private int folderId;
    private int userId;
    int frequency;
    int interval;
    String dayOfWeek;
    int cardNum;
    String coverPath;
    int pub;

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public String getDeckDescription() {
        return deckDescription;
    }

    public void setDeckDescription(String deckDescription) {
        this.deckDescription = deckDescription;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DeckEntity(String deckName, int completion, String deckDescription, String time, int frequency, String dayOfWeek, int interval,
                      int deckID, int userId, int folderId, String coverPath, int pub) {
        this.deckName = deckName;
        this.completion = completion;
        this.deckDescription = deckDescription;
        this.time = time;
        this.deckID = deckID;
        this.folderId = folderId;
        this.userId = userId;
        this.frequency = frequency;
        this.dayOfWeek = dayOfWeek;
        this.interval = interval;
        this.coverPath = coverPath;
        this.pub = pub;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public int getPub() {
        return pub;
    }

    public void setPub(int pub) {
        this.pub = pub;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDeckID() {
        return deckID;
    }

    public void setDeckID(int deckID) {
        this.deckID = deckID;
    }
}
