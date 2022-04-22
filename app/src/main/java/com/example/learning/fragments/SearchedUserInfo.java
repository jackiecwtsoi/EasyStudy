package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.learning.Difficulty;
import com.example.learning.R;
import com.example.learning.Row;

import java.util.ArrayList;

public class SearchedUserInfo extends Fragment {
    // define variables
    View rootView;

    SQLiteDatabase db;
    int userIdFromSearch;

    public SearchedUserInfo(SQLiteDatabase db) { this.db = db; }

    public static SearchedUserInfo newInstance(SQLiteDatabase db) {
        SearchedUserInfo fragment = new SearchedUserInfo(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friend_search_result, container, false);

        return rootView;
    }


}
