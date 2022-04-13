package com.example.learning;

public class Card {
    private String cardQuestion;
    private String cardAnswer;
    private int cardID;
    private int level;
    private String time;

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

    public Card(String cardQuestion, String cardAnswer, int cardID, int level, String time) {
        this.cardQuestion = cardQuestion;
        this.cardAnswer = cardAnswer;
        this.cardID = cardID;
        this.level = level;
        this.time = time;
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
