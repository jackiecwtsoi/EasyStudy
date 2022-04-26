package com.example.learning;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.Nullable;

import com.example.learning.fragments.ImageUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private DbApi dbApi;
    private ArrayList<String> names = new ArrayList<>();
    public static SQLiteOpenHelper mInstance;
    private Context context;
    public static synchronized SQLiteOpenHelper getmInstance(Context context){
        if (mInstance==null){
            mInstance=new MyDBOpenHelper(context,"elearningDB.db",null,1);
        }
        return mInstance;

    }

    public MyDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(u_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT(20),email TEXT(30),phone_number TEXT(20)," +
                "password TEXT(20), profile_picture TEXT(1000))");
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
                "time TEXT(1000)," +
                "cover_path TEXT, "+
                "public INTEGER,"+
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
        db.execSQL("CREATE TABLE message(" +
                "  message_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  u_id INTEGER NOT NULL," +
                "  date TEXT(1000)," +
                "  friend_id INTEGER NOT NULL," +
                "  status TEXT," +
                "  apply_content TEXT(1000)," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");

        db.execSQL("CREATE TABLE friend(" +
                "  record_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  u_id INTEGER NOT NULL," +
                "  friend_id INTEGER NOT NULL," +
                "  date TEXT(1000)," +
                "  status TEXT," +
                "  CONSTRAINT u_id FOREIGN KEY (u_id) REFERENCES user (u_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");


        names.add("Mike");
        names.add("Jasper");
        names.add("Amy");
        names.add("Ben");
        names.add("Ethan");
        names.add("Damon");
        
        dbApi = new DbApi(db);
        generateFakeUsers();
        generateFakeFolder();
        generateFakeFriends();
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private void generateFakeUsers(){
        for (int i = 0; i < 6; i++) {
            String name = names.get(i);
            // names.add(name);
            dbApi.insertUserFull(name, name+"@gmail.com", "123456",
                    "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/8394f798931623.5ee79b6a909ea.jpg");
        }
    }
    private void generateFakeFolder(){
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
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.default1);
        String path1 = ImageUtils.saveImageToGallery(context, bm, "default1.jpg");
        Bitmap bm2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.default2);
        String path2 = ImageUtils.saveImageToGallery(context, bm2, "default2.jpg");
        Bitmap bm3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.default3);
        String path3 = ImageUtils.saveImageToGallery(context, bm3, "default3.jpg");
        Bitmap bm4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.default4);
        String path4 = ImageUtils.saveImageToGallery(context, bm4, "default4.jpg");
        Bitmap bm5 = BitmapFactory.decodeResource(context.getResources(),R.drawable.default5);
        String path5 = ImageUtils.saveImageToGallery(context, bm5, "default5.jpg");
        Bitmap bm6 = BitmapFactory.decodeResource(context.getResources(),R.drawable.default6);
        String path6 = ImageUtils.saveImageToGallery(context, bm6, "default6.jpg");
        ArrayList<String> covers = new ArrayList<>();
        covers.add(path1);
        covers.add(path2);
        covers.add(path3);
        covers.add(path4);
        covers.add(path5);
        covers.add(path6);
        for (int i = 0; i < 6; i++) {
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
                    int pub = 1;
                    Random r = new Random();
                    String cover = covers.get(r.nextInt(6));

                    if (m == 5){
                        frequency = -1;
                        dayOfWeek = "";
                        interval = 0;
                        pub = 0;

                    }
                    else if (m % 3 == 0){
                        frequency = 0;
                        dayOfWeek = "Monday;Thursday";
                        interval = 0;
                        pub = 0;
//                        cover = path2;
                    }
                    else if(m % 3 == 1){
                        frequency = 1;
                        dayOfWeek = "Monday;";
                        interval = 0;
//                        cover = path3;
                    }
                    else{
                        frequency = 2;
                        dayOfWeek = "";
                        interval = 4;
//                        cover = path4;
                    }

                    long deckid = dbApi.insertDeck(deckName, deckDescription, 0, frequency, dayOfWeek, interval, (int)folderid, i + 1, cover, pub);
//                    Random r = new Random();
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

    private void generateFakeFriends(){
        dbApi.insertFriend(1, 3, FriendStatus.FRIEND_REQUESTED); // Amy requested to be Mike's friend
        dbApi.insertFriend(1, 6, FriendStatus.FRIEND_REQUESTED); // Damon requested to be Mike's friend
        dbApi.insertFriend(1, 5, FriendStatus.FRIEND); // Ethan is already Mike's friend
        dbApi.insertFriend(5, 1, FriendStatus.FRIEND); // replicate of the row above to indicate friendship
        dbApi.insertFriend(1, 4, FriendStatus.FRIEND); // Ben is already Mike's friend
        dbApi.insertFriend(4, 1, FriendStatus.FRIEND);
        dbApi.insertFriend(2, 1, FriendStatus.FRIEND_REQUESTED); // Mike requested to be Jasper's friend
        dbApi.insertFriend(3, 2, FriendStatus.FRIEND_REQUESTED); // Jasper requested to be Amy's friend
        dbApi.insertFriend(3, 6, FriendStatus.FRIEND); // Damon is already Amy's friend
        dbApi.insertFriend(6, 3, FriendStatus.FRIEND);
    }
}