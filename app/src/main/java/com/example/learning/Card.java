package com.example.learning;

public class Card {
    private String cardQuestion;
    private String cardAnswer;
    private int cardID;
    private int level;
    private String time;
    private int folderId;
    private int userId;
    private int deckId;

    public String getCardQuestion() {
        return cardQuestion;
    }

    public void setCardQuestion(String cardQuestion) {
        this.cardQuestion = cardQuestion;
    }

    public String getCardAnswer() {
        return cardAnswer;
    }

    public void setCardAnswer(String cardAnswer) {
        this.cardAnswer = cardAnswer;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
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

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public Card(String cardQuestion, String cardAnswer, int cardID, int level, String time, int folderId, int userId, int deckId) {
        this.cardQuestion = cardQuestion;
        this.cardAnswer = cardAnswer;
        this.cardID = cardID;
        this.level = level;
        this.time = time;
        this.deckId = deckId;
        this.userId = userId;
        this.folderId = folderId;

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
