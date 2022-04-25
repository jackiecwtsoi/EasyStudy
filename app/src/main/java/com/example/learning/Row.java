package com.example.learning;

import com.example.learning.fragments.Folder;

public class Row {
    //private FolderEntity folder;
    private DeckEntity deck;
    private Card card;
    private Difficulty rating;

    public Row(DeckEntity deck, Card card) {
        //this.folder = folder;
        this.deck = deck;
        this.card = card;
        this.rating = Difficulty.NONE;
    }

//    public FolderEntity getFolder() {
//        return this.folder;
//    }

    public DeckEntity getDeck() {
        return this.deck;
    }

    public Card getCard() {
        return this.card;
    }

    public Difficulty getRating() {
        return this.rating;
    }

    public void setRow(DeckEntity deck, Card card) {
        //this.folder = folder;
        this.deck = deck;
        this.card = card;
    }

    public void setRating(Difficulty rating) {
        this.rating = rating;
    }
}
