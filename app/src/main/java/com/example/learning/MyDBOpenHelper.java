package com.example.learning;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {super(context, "elearning.db", null, 1); }
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
                "  time TEXT(1000)," +
                "  CONSTRAINT folder_id FOREIGN KEY (folder_id) REFERENCES folder (folder_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");
        db.execSQL("CREATE TABLE card(" +
                "  card_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  deck_id INTEGER NOT NULL," +
                "  u_id INTEGER NOT NULL," +
                "  card_question TEXT(1000)," +
                "  card_answer TEXT(1000)," +
                "  time TEXT(1000)," +
                "  level INTEGER," +
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


    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}