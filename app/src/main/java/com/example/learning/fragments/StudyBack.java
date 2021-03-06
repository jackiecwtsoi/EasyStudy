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

import com.example.learning.Card;
import com.example.learning.DbApi;
import com.example.learning.Difficulty;
import com.example.learning.R;
import com.example.learning.Row;

import java.util.ArrayList;

public class StudyBack extends DialogFragment {
    SQLiteDatabase db;

    // define variables
    View rootView;
    TextView textCardAnswerContent;
    CardView cardStudyBack;
    Button btnEasy, btnHard, btnForgot;

    static ArrayList<Row> STUDY_LIST = StudyFront.STUDY_LIST;
    int rowIdx;

    public StudyBack(SQLiteDatabase db) {
        this.db = db;
    }

    public static StudyBack newInstance(SQLiteDatabase db) {
        StudyBack fragment = new StudyBack(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rowIdx = bundle.getInt("rowIdx");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_study_back, container, false);
        textCardAnswerContent = rootView.findViewById(R.id.textCardAnswerContent);
        cardStudyBack = rootView.findViewById(R.id.cardStudyBack);
        btnEasy = rootView.findViewById(R.id.btnEasy);
        btnHard = rootView.findViewById(R.id.btnHard);
        btnForgot = rootView.findViewById(R.id.btnForgot);

        if ((rowIdx >= 0) && (rowIdx < STUDY_LIST.size())) {
            Row row = STUDY_LIST.get(rowIdx);
            textCardAnswerContent.setText(row.getCard().getCardAnswer());
//            textCardAnswerContent.setText(row.get(2));
        }

        // tap on the popup fragment returns user back to the front card (question side)
        cardStudyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Row row = STUDY_LIST.get(rowIdx);
                setRating(row, Difficulty.EASY);
                dismiss();
            }
        });
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Row row = STUDY_LIST.get(rowIdx);
                setRating(row, Difficulty.HARD);
                dismiss();
            }
        });
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Row row = STUDY_LIST.get(rowIdx);
                setRating(row, Difficulty.FORGOT);
                dismiss();
            }
        });


        return rootView;
    }

    private void setRating(Row row, Difficulty difficulty) {
        // first update the level in the local study list
        row.setRating(difficulty);

        // second update the level in the database
        Card card = row.getCard();
        DbApi dbapi = new DbApi(this.db);
        dbapi.updateCardLevel(card.getFolderId(), card.getDeckId(), card.getCardID(), difficulty);
    }

}