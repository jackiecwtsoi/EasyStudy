package com.example.learning.fragments;

import android.os.Bundle;

import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.learning.R;

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
    Button btnOpenLastOpenDeck, btnTasksToday;
    Boolean ALL_CARDS = true; // indicates whether we study ALL CARDS from the database

    public Study() {
        // Required empty public constructor
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
    public static Study newInstance(String param1, String param2) {
        Study fragment = new Study();
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
        btnOpenLastOpenDeck = rootView.findViewById(R.id.btnOpenLastOpenDeck);
        btnTasksToday = rootView.findViewById(R.id.btnReviewAllCards);

        // when the "Review Unfinished Cards" button is pressed, we only show the unfinished cards
        btnOpenLastOpenDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyFront studyFront = new StudyFront();

                Bundle bundle = new Bundle();
                ALL_CARDS = false; // since we do not want to study ALL CARDS from the database
                bundle.putBoolean("ALL_CARDS", ALL_CARDS);
                studyFront.setArguments(bundle);

                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutStudy, studyFront)
                        .commit();

            }
        });

        // when the "Review All Cards" button is pressed, we show EVERY CARD in the database
        btnTasksToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyFront studyFront = new StudyFront();

                Bundle bundle = new Bundle();
                ALL_CARDS = true; // since we do not want to study ALL CARDS from the database
                bundle.putBoolean("ALL_CARDS", ALL_CARDS);
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