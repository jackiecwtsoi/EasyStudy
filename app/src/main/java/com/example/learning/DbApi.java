package com.example.learning;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DbApi {
    private SQLiteDatabase db;

    public DbApi(SQLiteDatabase db) {
       this.db = db;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
//        System.out.println(formatter.format(date));
        return formatter.format(date);
    }

    public static String randomName(int min, int max) {
        String name;
        char[] nameChar;

        int nameLength = (int) (Math.random() * (max - min + 1)) + min;
        nameChar = new char[nameLength];
        nameChar[0] = (char) (Math.random() * 26 + 65);
        for (int i = 1; i < nameLength; i++) {
            nameChar[i] = (char) (Math.random() * 26 + 97);
        }
        name = new String(nameChar);
        return name;
    }

    public ArrayList<Card> queryCard(int deckID, int folderID, int userID) {
        ArrayList<Card> cards = new ArrayList<>();

        Cursor fcursor = db.query("card", null, null, null, null, null, null);
        if (fcursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int uid = fcursor.getInt(fcursor.getColumnIndex("u_id"));
                @SuppressLint("Range") int folder_id = fcursor.getInt(fcursor.getColumnIndex("folder_id"));
                @SuppressLint("Range") int deck_id = fcursor.getInt(fcursor.getColumnIndex("deck_id"));
                if (userID == uid && folder_id == folderID && deck_id == deckID) {
                    @SuppressLint("Range") int fid = fcursor.getInt(fcursor.getColumnIndex("card_id"));
                    @SuppressLint("Range") String question = fcursor.getString(fcursor.getColumnIndex("card_question"));
                    @SuppressLint("Range") String answer = fcursor.getString(fcursor.getColumnIndex("card_answer"));
                    @SuppressLint("Range") String time = fcursor.getString(fcursor.getColumnIndex("time"));
                    @SuppressLint("Range") int level = fcursor.getInt(fcursor.getColumnIndex("level"));
                    Card card = new Card(question, answer, fid, level, time);
                    cards.add(card);
                }
            } while (fcursor.moveToNext());
        }
        fcursor.close();
        return cards;
    }

    public ArrayList<DeckEntity> queryDeck(int folderID, int userID) {
        ArrayList<DeckEntity> deckEntities = new ArrayList<>();

        Cursor fcursor = db.query("deck", null, null, null, null, null, null);
        if (fcursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int uid = fcursor.getInt(fcursor.getColumnIndex("u_id"));
                @SuppressLint("Range") int folder_id = fcursor.getInt(fcursor.getColumnIndex("folder_id"));
                if (userID == uid && folder_id == folderID) {
                    @SuppressLint("Range") int fid = fcursor.getInt(fcursor.getColumnIndex("deck_id"));
                    @SuppressLint("Range") String name = fcursor.getString(fcursor.getColumnIndex("deck_name"));
                    @SuppressLint("Range") String description = fcursor.getString(fcursor.getColumnIndex("deck_description"));
                    @SuppressLint("Range") String time = fcursor.getString(fcursor.getColumnIndex("time"));
                    @SuppressLint("Range") int completion = fcursor.getInt(fcursor.getColumnIndex("completion"));
                    DeckEntity deckEntity = new DeckEntity(name, completion, description, time, fid);
                    deckEntities.add(deckEntity);
                }
            } while (fcursor.moveToNext());
        }
        fcursor.close();
        return deckEntities;
    }

    public ArrayList<FolderEntity> queryFolder(int userID) {
        ArrayList<FolderEntity> folders = new ArrayList<>();
        Cursor fcursor = db.query("folder", null, null, null, null, null, null);
        if (fcursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int uid = fcursor.getInt(fcursor.getColumnIndex("u_id"));

                if (userID == uid) {
                    @SuppressLint("Range") int fid = fcursor.getInt(fcursor.getColumnIndex("folder_id"));
                    @SuppressLint("Range") String name = fcursor.getString(fcursor.getColumnIndex("folder_name"));
                    @SuppressLint("Range") String description = fcursor.getString(fcursor.getColumnIndex("folder_description"));
                    @SuppressLint("Range") String time = fcursor.getString(fcursor.getColumnIndex("time"));
                    FolderEntity folder = new FolderEntity(name, description, fid, time);
                    folders.add(folder);
                }
            } while (fcursor.moveToNext());
        }
        fcursor.close();
        return folders;
    }

    public void insertFolder(String folderName, String folderDescription, int userID) {
        int[] arrary = new int[1000];
        boolean justice = false;
        int count = 0;

        Cursor check_cursor = db.query("user", null, null, null, null, null, null);
        if (check_cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int pid = check_cursor.getInt(check_cursor.getColumnIndex("u_id"));
                arrary[count] = pid;
                count = count + 1;
            } while (check_cursor.moveToNext());

        }
        for (int i = 0; i < arrary.length; i++) {
            if (arrary[i] == userID) {
                justice = true;
            }
        }


        if (justice == true) {
            ContentValues values2 = new ContentValues();
            values2.put("folder_name", folderName);
            values2.put("folder_description", folderDescription);
            values2.put("u_id", userID);
            String time = getDate();
            values2.put("time", time);
            db.insert("folder", null, values2);
            System.out.println("create folder: " + folderName + "with description: " + folderDescription + "for user:" + userID);

        } else {
            System.out.println("not create folder: " + folderName + "with description: " + folderDescription + "for user:" + userID);
        }
    }
    public void insertUser(String User_name) {
        if (User_name != null) {
            ContentValues values1 = new ContentValues();
            values1.put("name", User_name);
            db.insert("user", null, values1);
            System.out.println("create user: " + User_name);

        } else {
            System.out.println("not create user: " + User_name);
        }
    }
    public void insertDeck(String deckName, String deckDescription, int completion, int folderID, int userID) {
        int[] arrary = new int[1000];
        boolean justice = false;
        int count = 0;

        Cursor check_cursor = db.query("folder", null, null, null, null, null, null);
        if (check_cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int pid = check_cursor.getInt(check_cursor.getColumnIndex("folder_id"));
                arrary[count] = pid;
                count = count + 1;
            } while (check_cursor.moveToNext());

        }
        for (int i = 0; i < arrary.length; i++) {
            if (arrary[i] == folderID) {
                justice = true;
            }
        }

        if (justice == true) {
            ContentValues values2 = new ContentValues();
            values2.put("deck_name", deckName);
            values2.put("deck_description", deckDescription);
            values2.put("folder_id", folderID);
            values2.put("completion", completion);
            values2.put("u_id", userID);
            String time = getDate();
            values2.put("time", time);
            db.insert("deck", null, values2);
            System.out.println("create folder: " + deckName + "with description: " + deckDescription + "for user:" + folderID);

        } else {
            System.out.println("not create folder: " + deckName + "with description: " + deckDescription + "for user:" + folderID);
        }
    }

    public void insertCard(String cardName, String cardQuestion, String cardAnswer, int hardness, int deckID, int folderID, int userID) {
        int[] arrary = new int[1000];
        boolean justice = false;
        int count = 0;

        Cursor check_cursor = db.query("deck", null, null, null, null, null, null);
        if (check_cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int pid = check_cursor.getInt(check_cursor.getColumnIndex("deck_id"));
                arrary[count] = pid;
                count = count + 1;
            } while (check_cursor.moveToNext());

        }
        for (int i = 0; i < arrary.length; i++) {
            if (arrary[i] == deckID) {
                justice = true;
            }
        }


        if (justice == true) {
            ContentValues values2 = new ContentValues();
            // values2.put("card_name", cardName);
            values2.put("card_question", cardQuestion);
            values2.put("card_answer", cardAnswer);
            values2.put("deck_id", deckID);
            values2.put("folder_id", folderID);
            values2.put("level", hardness);
            values2.put("u_id", userID);
            String time = getDate();
            values2.put("time", time);
            db.insert("card", null, values2);
            System.out.println("create folder: " + cardName + "with answer: " + cardAnswer + "for user:" + deckID);

        } else {
            System.out.println("not create folder: " + cardName + "with description: " + cardAnswer + "for user:" + deckID);
        }
    }

    public ArrayList<Row> getAllCards(int userID) {
        ArrayList<Row> allCards = new ArrayList<>();
        ArrayList<FolderEntity> folders = queryFolder(userID);
        for (FolderEntity folder : folders) {
            ArrayList<DeckEntity> decks = queryDeck(folder.getFolderID(), userID);
            for (DeckEntity deck : decks) {
                ArrayList<Card> cards = queryCard(deck.getDeckID(), folder.getFolderID(), userID);
                for (Card card : cards) {
                    Row row = new Row(folder, deck, card);
                    allCards.add(row);
                }
            }
        }
        return allCards;
    }

}
