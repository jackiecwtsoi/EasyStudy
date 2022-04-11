package com.example.learning;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class create_database extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_database);

    }


    public void createDB(View view) {
        SQLiteOpenHelper helper= MyDBOpenHelper.getmInstance(this);
        SQLiteDatabase readableDB=helper.getReadableDatabase();

    }

    public void SearchDB(View view) {
        SQLiteOpenHelper helper = MyDBOpenHelper.getmInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()){

            Cursor cursor = db.rawQuery("select * from user", null);
            while (cursor.moveToNext()){
                @SuppressLint("Range") int F_id = cursor.getInt(cursor.getColumnIndex("u_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("elearning","query_id"+F_id+" name"+name);


            }
            cursor.close();
            db.close();
        }
    }
}