package com.example.learning;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyDBOpenHelper myDBHelper = new MyDBOpenHelper(context, "elearning.db", null, 1);
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        DbApi dbapi = new DbApi(db);

        int userId = intent.getIntExtra("userId", 0);
        String username = dbapi.queryUserName(userId);

        Calendar calendar = Calendar.getInstance();
        int intDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = dbapi.getDayOfWeek(intDayOfWeek);

        ArrayList<DeckEntity> decksForReminder = dbapi.getDecksForReminder(userId, dayOfWeek);

        System.out.println("userId: " + userId + ": " + username);
        System.out.println("decks to study for: " + decksForReminder.size());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent repeatingIntent = new Intent(context, RepeatingReminderActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyTest")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar))
                .setContentTitle("Study Time!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(
                        "Hello " + username + "! Today is " + dayOfWeek + ", and you have "  +
                                decksForReminder.size() + " unfinished decks to study for. " +
                                "Click this notification to start studying.")
                )
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}
