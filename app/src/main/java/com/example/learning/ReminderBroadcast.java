package com.example.learning;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderBroadcast extends BroadcastReceiver {
//    SQLiteDatabase db;

//    public ReminderBroadcast() {
//        super();
//    }
//
//    public ReminderBroadcast(SQLiteDatabase db) {
//        this.db = db;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int userId = intent.getIntExtra("userId", 0);
        Calendar calendar = Calendar.getInstance();
        int intDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = "";

        switch (intDayOfWeek) {
            case 1: dayOfWeek="Sunday"; break;
            case 2: dayOfWeek="Monday"; break;
            case 3: dayOfWeek="Tuesday"; break;
            case 4: dayOfWeek="Wednesday"; break;
            case 5: dayOfWeek="Thursday"; break;
            case 6: dayOfWeek="Friday"; break;
            case 7: dayOfWeek="Saturday"; break;
        }

        MyDBOpenHelper myDBHelper = new MyDBOpenHelper(context, "elearning.db", null, 1);
        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        DbApi dbapi = new DbApi(db);
        ArrayList<DeckEntity> decksForReminder = dbapi.getDecksForReminder(userId, "Monday");

        System.out.println(userId);
        System.out.println(decksForReminder.size());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent repeatingIntent = new Intent(context, RepeatingReminderActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyTest")
                .setSmallIcon(R.drawable.avatar)
                .setContentTitle("Monday")
                .setContentText("Hi! The time is " + calendar.getTime() + ".")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}
