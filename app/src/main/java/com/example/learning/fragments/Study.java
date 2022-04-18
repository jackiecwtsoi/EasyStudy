package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.learning.R;
import com.example.learning.StudyType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Study#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Study extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // define variables
    View rootView;
    Button btnReviewUnfinishedCards, btnReviewAllCards, btnReviewTodayCards;
    RecyclerView listTasksToday;
    StudyType STUDY_TYPE = StudyType.ALL_CARDS; // indicates whether we study ALL CARDS from the database
    SQLiteDatabase db;

    public Study(SQLiteDatabase db) {
        // Required empty public constructor
        this.db = db;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Study.
     */
    // TODO: Rename and change types and number of parameters
    public static Study newInstance(String param1, String param2, SQLiteDatabase db) {
        Study fragment = new Study(db);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeView(container.findViewById(R.id.cardStudyDoneOverall));
            container.removeView(container.findViewById(R.id.btnReviewAgain));
            container.removeView(container.findViewById(R.id.btnReturnStudyOverview));
        }

        rootView = inflater.inflate(R.layout.fragment_study, container, false);
        btnReviewUnfinishedCards = rootView.findViewById(R.id.btnOpenLastOpenDeck);
        btnReviewTodayCards = rootView.findViewById(R.id.btnReviewTodayCards);
        btnReviewAllCards = rootView.findViewById(R.id.btnReviewAllCards);
        listTasksToday = rootView.findViewById(R.id.listTasksToday);


        // when the "Review Unfinished Cards" button is pressed, we only show the unfinished cards
        btnReviewUnfinishedCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyFront studyFront = new StudyFront(db);

                Bundle bundle = new Bundle();
                STUDY_TYPE = StudyType.TODAY_REMAINING_CARDS; // since we do not want to study ALL CARDS from the database
                bundle.putSerializable("STUDY_TYPE", STUDY_TYPE);
                studyFront.setArguments(bundle);

                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutStudy, studyFront)
                        .commit();

            }
        });

        // when the "Review Today's Cards" button is pressed, we show the decks that need to be REMINDED TODAY
        btnReviewTodayCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyFront studyFront = new StudyFront(db);

                Bundle bundle = new Bundle();
                STUDY_TYPE = StudyType.TODAY_MUST_STUDY; // since we want to only study the decks for today
                bundle.putSerializable("STUDY_TYPE", STUDY_TYPE);
                studyFront.setArguments(bundle);

                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutStudy, studyFront)
                        .commit();
            }
        });


        // when the "Review All Cards" button is pressed, we show EVERY CARD in the database
        btnReviewAllCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyFront studyFront = new StudyFront(db);

                Bundle bundle = new Bundle();
                STUDY_TYPE = StudyType.ALL_CARDS; // since we want to study ALL CARDS from the database
                bundle.putSerializable("STUDY_TYPE", STUDY_TYPE);
                studyFront.setArguments(bundle);

                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutStudy, studyFront)
                        .commit();
            }
        });



        return rootView;
    }
}