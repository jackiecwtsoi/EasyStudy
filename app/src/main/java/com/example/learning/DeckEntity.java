package com.example.learning;

public class DeckEntity {
    private String deckName;
    private int completion;
    private String deckDescription;
    private String time;
    private int deckID;

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

    public DeckEntity(String deckName, int completion, String deckDescription, String time, int deckID) {
        this.deckName = deckName;
        this.completion = completion;
        this.deckDescription = deckDescription;
        this.time = time;
        this.deckID = deckID;
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
