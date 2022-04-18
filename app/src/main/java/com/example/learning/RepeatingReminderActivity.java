package com.example.learning;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RepeatingReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_study);

        Intent openAppIntent = new Intent(this, MainActivity.class);
        openAppIntent.setAction(Intent.ACTION_MAIN);
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(openAppIntent);
    }
}
