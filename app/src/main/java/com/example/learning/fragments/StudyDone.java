package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.learning.Card;
import com.example.learning.DbApi;
import com.example.learning.Difficulty;
import com.example.learning.R;
import com.example.learning.Row;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StudyDone extends Fragment {
    // define variables
    View rootView;
    Button btnReviewAgain, btnReturnToStudyOverview;
    PieChart pieTodayProgress;
    PieDataSet pieDataSetTodayProgress;
    PieData pieDataTodayProgress;
    List<PieEntry> pieEntryList;
    int nEasy=0, nHard=0, nForgot=0, nComplete=0, nIncomplete=0;

    static ArrayList<Row> STUDY_LIST = StudyFront.STUDY_LIST;
    static boolean CLEAR_RATINGS = true; // clear all ratings every time we review all
    static boolean INTERACT_ENABLE = false; // do not allow interaction with pie chart
    static boolean DESCRIPTION_ENABLE = false; // do not enable description in the pie chart
    static boolean LEGEND_ENABLE = false; // do not enable legends in the pie chart
    SQLiteDatabase db;

    public StudyDone(SQLiteDatabase db) {
        this.db = db;
    }

    public static StudyDone newInstance(String param1, String param2, SQLiteDatabase db) {
        StudyDone fragment = new StudyDone(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        if (container != null) {
            container.removeAllViews();
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_study_done, container, false);
        btnReviewAgain = rootView.findViewById(R.id.btnReviewAgain);
        btnReturnToStudyOverview = rootView.findViewById(R.id.btnReturnStudyOverview);
        pieTodayProgress = rootView.findViewById(R.id.pieTodayProgress);

        // show pie chart of today's study progress
        this.analyzeTodayProgress();

        btnReviewAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRatings(CLEAR_RATINGS); // clear all ratings in STUDY_LIST

                StudyFront studyFront = new StudyFront(db);
                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutStudyDone, studyFront)
                        .commit();
            }

        });

        btnReturnToStudyOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRatings(CLEAR_RATINGS);

                Study study = new Study(db);
                FragmentManager studyManager = getFragmentManager();
                studyManager.beginTransaction()
                        .replace(R.id.layoutStudyDone, study)
                        .commit();
            }
        });

        return rootView;
    }

    // method to pass data into the pie chart
    public void analyzeTodayProgress() {
        // iterate through to-study list to count statistics
        for (Row row: STUDY_LIST) {
            switch (row.getRating()) {
                case EASY:
                    this.nEasy++;
                    break;
                case HARD:
                    this.nHard++;
                    break;
                case FORGOT:
                    this.nForgot++;
                    break;
                case NONE:
                    this.nIncomplete++;
                    break;
            }
        }

        this.nComplete = this.nEasy + this.nHard + this.nForgot;
        this.pieEntryList = new ArrayList<>();
//        this.pieEntryList.add(new PieEntry(nEasy, "Easy"));
//        this.pieEntryList.add(new PieEntry(nHard, "Hard"));
//        this.pieEntryList.add(new PieEntry(nForgot, "Forgot"));
        this.pieEntryList.add(new PieEntry(nComplete, "Complete"));
        this.pieEntryList.add(new PieEntry(nIncomplete, "Incomplete"));

        // customize the pie chart
        this.pieDataSetTodayProgress = new PieDataSet(pieEntryList, "Rating");
        this.pieDataSetTodayProgress.setColors(ColorTemplate.JOYFUL_COLORS);
        this.pieDataSetTodayProgress.setValueTextSize(16);

        this.pieDataTodayProgress = new PieData(this.pieDataSetTodayProgress);
        this.pieTodayProgress.setData(pieDataTodayProgress);

        // customize more
//        this.pieDataTodayProgress.setValueFormatter(new PercentFormatter(this.pieTodayProgress));
        this.pieTodayProgress.setTouchEnabled(INTERACT_ENABLE);
        this.pieTodayProgress.setUsePercentValues(true);
        this.pieTodayProgress.setDrawHoleEnabled(false);
        this.pieTodayProgress.getDescription().setEnabled(DESCRIPTION_ENABLE);
        this.pieTodayProgress.getLegend().setEnabled(LEGEND_ENABLE);
        this.pieTodayProgress.invalidate();


        System.out.printf("%d %d %d %d\n",nEasy, nHard, nForgot, nIncomplete);

    }

    // method to clear all ratings in the STUDY_LIST
    public void clearRatings(boolean clear) {
        if (clear) {
            for (Row row: STUDY_LIST) {
                row.setRating(Difficulty.NONE); // set rating in the STUDY_LIST
            }
        }
    }
}