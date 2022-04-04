package com.example.learning.fragments;

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

import com.example.learning.R;

import java.util.ArrayList;

public class StudyBack extends DialogFragment {
    // define variables
    View rootView;
    TextView textCardAnswerContent;
    CardView cardStudyBack;
    Button btnEasy, btnHard, btnForgot;

    static ArrayList<ArrayList<String>> STUDY_LIST = StudyFront.STUDY_LIST;
    int rowIdx;
    // define variables that come from the bundle
//    static int rowIdx = StudyFront.rowIdx;

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
            ArrayList<String> row = STUDY_LIST.get(rowIdx);
            textCardAnswerContent.setText(row.get(2));
        }

        // tap on the popup fragment returns user back to the front card (question side)
        cardStudyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // TODO: tap on any of the 3 rating buttons returns the next question in the to-study list
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STUDY_LIST.get(rowIdx).size() >= 4) {
                    STUDY_LIST.get(rowIdx).remove(3);
                }
                STUDY_LIST.get(rowIdx).add("Easy");
                dismiss();
            }
        });
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STUDY_LIST.get(rowIdx).size() >= 4) {
                    STUDY_LIST.get(rowIdx).remove(3);

                }
                STUDY_LIST.get(rowIdx).add("Hard");
                dismiss();
            }
        });
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STUDY_LIST.get(rowIdx).size() >= 4) {
                    STUDY_LIST.get(rowIdx).remove(3);
                }
                STUDY_LIST.get(rowIdx).add("Forgot");
                dismiss();
            }
        });


        return rootView;
    }

}