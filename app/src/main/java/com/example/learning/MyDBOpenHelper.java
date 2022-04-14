package com.example.learning;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private DbApi dbApi;
    private ArrayList<String> names = new ArrayList<>();
    public static SQLiteOpenHelper mInstance;
    public static synchronized SQLiteOpenHelper getmInstance(Context context){
        if (mInstance==null){
            mInstance=new MyDBOpenHelper(context,"elearningDB.db",null,1);
        }
        return mInstance;

    }

    public MyDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(u_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT(20),email TEXT(30),phone_number TEXT(20)," +
                "password TEXT(20) )");
        db.execSQL("CREATE TABLE folder(folder_id INTEGER PRIMARY KEY AUTOINCREMENT,folder_name TEXT,time TEXT,folder_description TEXT, u_id INTEGER," +
                "CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE)");
        db.execSQL("CREATE TABLE deck(" +
                "  deck_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  folder_id INTEGER NOT NULL," +
                "  u_id INTEGER NOT NULL," +
                "  deck_name TEXT(1000)," +
                "  deck_description TEXT(1000)," +
                " completion INTEGER, " +
                "frequency INTEGER, "+
                "day_of_week TEXT,"+
                "interval INTEGER, "+
                "  time TEXT(1000)," +
                "  CONSTRAINT folder_id FOREIGN KEY (folder_id) REFERENCES folder (folder_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");
        db.execSQL("CREATE TABLE card(" +
                "  card_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  deck_id INTEGER NOT NULL," +
                "  u_id INTEGER NOT NULL," +
                "  folder_id INTEGER NOT NULL,"+
                "  card_question TEXT(1000)," +
                "  card_answer TEXT(1000)," +
                "  time TEXT(1000)," +
                "  level INTEGER," +
                "  CONSTRAINT folder_id FOREIGN KEY (folder_id) REFERENCES folder (folder_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "  CONSTRAINT deck_id FOREIGN KEY (deck_id) REFERENCES deck (deck_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");
        db.execSQL("CREATE TABLE comment(" +
                "  comment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  u_id INTEGER NOT NULL," +
                "  comment_text TEXT(2000)," +
                "  time TEXT(100)," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");
        db.execSQL("CREATE TABLE sign(" +
                "  sign_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  u_id INTEGER NOT NULL," +
                "  date TEXT(1000)," +
                "  status TEXT," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");
        db.execSQL("CREATE TABLE alarm(" +
                "  alarm_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  u_id INTEGER NOT NULL," +
                "  date TEXT(1000)," +
                "  status TEXT," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");

        names.add("Mike");
        names.add("Jasper");
        names.add("Amy");
        
        dbApi = new DbApi(db);
        generateFakeUsers();
        genrateFakeFolder();
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private void generateFakeUsers(){
        for (int i = 0; i < 3; i++) {
            String name = names.get(i);
            // names.add(name);
            dbApi.insertUser(name);
        }
    }
    private void genrateFakeFolder(){
        ArrayList<String> folders = new ArrayList<>();
        folders.add("English");
        folders.add("Biology");
        folders.add("Physics");
        folders.add("Math");
        folders.add("Chinese");
        folders.add("Chemistry");
        ArrayList<String> decks = new ArrayList<>();
        decks.add("Section 1");
        decks.add("Section 2");
        decks.add("Section 3");
        decks.add("Section 4");
        decks.add("Section 5");
        decks.add("Section 6");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                String folderName = folders.get(j) + " for " + names.get(i);
                String folderDescription = "This is the description for " + folderName;
                long folderid = dbApi.insertFolder(folderName, folderDescription, i + 1);
                for (int m = 0; m < 6; m++) {
                    String deckName = decks.get(m) + " for " + folderName;
                    String deckDescription = "This is the description for " + deckName;
                    int frequency = -1;
                    String dayOfWeek = "";
                    int interval = 0;
                    if (m == 5){
                        frequency = -1;
                        dayOfWeek = "";
                        interval = 0;
                    }
                    else if (m % 3 == 0){
                        frequency = 0;
                        dayOfWeek = "Monday;Thursday";
                        interval = 0;
                    }
                    else if(m % 3 == 1){
                        frequency = 1;
                        dayOfWeek = "Monday;";
                        interval = 0;
                    }
                    else{
                        frequency = 2;
                        dayOfWeek = "";
                        interval = 4;
                    }
                    long deckid = dbApi.insertDeck(deckName, deckDescription, 0, frequency, dayOfWeek, interval, (int)folderid, i + 1);
                    Random r = new Random();
                    for (int n = 0; n < 4; n++) {
                        String cardName = "Card" + Integer.toString(n) + " for " + deckName;
                        int a = r.nextInt(10);
                        int b = r.nextInt(10);
                        String cardQuestion = Integer.toString(a) + '+' + Integer.toString(b) + "= ? ";
                        String cardAnswer = Integer.toString(a + b);
                        int hardness = r.nextInt(2);
                        dbApi.insertCard(cardName, cardQuestion, cardAnswer, hardness, (int)deckid, (int)folderid, i + 1);
                    }
                }
            }
        }
    }
}