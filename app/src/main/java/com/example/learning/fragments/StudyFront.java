package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.Card;
import com.example.learning.DbApi;
import com.example.learning.DeckEntity;
import com.example.learning.FolderEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.Row;
import com.example.learning.StudyType;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Calendar;

public class StudyFront extends Fragment {
    // define variables
    View rootView;
    TextView textDeckTitle, textCardQuestionContent, textStudyProgress;
    CardView cardStudyFront;
    Button btnNextCard, btnPreviousCard;
    Button btnAbortStudy;
    ProgressBar progressBar;
    StudyType STUDY_TYPE; // indicates whether we study ALL CARDS from the database
    DeckEntity selected_deck;

    static int rowIdx; // index for locating a row in the study list
    static ArrayList<Row> STUDY_LIST;

    private OnFragmentInteractionListener mListener;
    SQLiteDatabase db;
    int userId;
    Calendar calendar = Calendar.getInstance();
    String dayOfWeek;

    public StudyFront(SQLiteDatabase db) {
        this.db = db;
    }

    public static StudyFront newInstance(SQLiteDatabase db) {
        StudyFront fragment = new StudyFront(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            STUDY_TYPE = (StudyType) bundle.getSerializable("STUDY_TYPE");
            if (STUDY_TYPE == StudyType.SELECTED_DECK) {
                selected_deck = (DeckEntity) bundle.getSerializable("selected_deck");
            }
        }
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

        rootView = inflater.inflate(R.layout.fragment_study_front, container, false);

        DbApi dbapi = new DbApi(this.db);

        textDeckTitle = rootView.findViewById(R.id.textDeckTitle);
        textCardQuestionContent = rootView.findViewById(R.id.textCardQuestionContent);
        textStudyProgress = rootView.findViewById(R.id.textStudyProgress);
        cardStudyFront = rootView.findViewById(R.id.cardStudyFront);
        btnNextCard = rootView.findViewById(R.id.btnNextCard);
        btnPreviousCard = rootView.findViewById(R.id.btnPreviousCard);
        progressBar = rootView.findViewById(R.id.progressBar);
        btnAbortStudy = rootView.findViewById(R.id.btnAbortStudy);

        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();
        dayOfWeek = dbapi.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

        // initialize the first card to be the first in the to-study list
        rowIdx = 0;

        if (STUDY_TYPE != null) {
            switch (STUDY_TYPE) {
                case ALL_CARDS:
                    STUDY_LIST = dbapi.getAllCards(userId);
                    break;
                case TODAY_MUST_STUDY:
                    STUDY_LIST = dbapi.getCardsForReminder(userId, dayOfWeek);
                    break;
                case SELECTED_DECK:
                    STUDY_LIST = dbapi.getCardsFromDeck(selected_deck);
                    break;
            }
        }

        // define progress bar
        progressBar.setMax(STUDY_LIST.size());
        progressBar.setProgress(1); // initialize progress

        textDeckTitle.setText(STUDY_LIST.get(rowIdx).getDeck().getDeckName());
        textCardQuestionContent.setText(STUDY_LIST.get(rowIdx).getCard().getCardQuestion());
        textStudyProgress.setText("Card " + String.valueOf(rowIdx+1) + " / " + String.valueOf(STUDY_LIST.size()));

        // abort study session button: return to StudyDone fragment when clicked
        btnAbortStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowIdx = STUDY_LIST.size();

                // change to StudyDone fragment
                StudyDone studyDone = new StudyDone(db);
                FragmentManager studyManager = getFragmentManager();
                studyManager.beginTransaction()
                        .replace(R.id.layoutStudyFront, studyDone)
                        .commit();
            }
        });

        btnNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rowIdx < STUDY_LIST.size()-1) {
                    Row row = STUDY_LIST.get(++rowIdx);
                    textDeckTitle.setText(row.getDeck().getDeckName());
                    textCardQuestionContent.setText(row.getCard().getCardQuestion());
                    textStudyProgress.setText("Card " + String.valueOf(rowIdx+1) + " / " + String.valueOf(STUDY_LIST.size()));
                    progressBar.setProgress(rowIdx+1);
                }
                else {
                    rowIdx = STUDY_LIST.size();

                    // change to StudyDone fragment
                    StudyDone studyDone = new StudyDone(db);
                    FragmentManager studyManager = getFragmentManager();
                    studyManager.beginTransaction()
                            .replace(R.id.layoutStudyFront, studyDone)
                            .commit();
                }
            }
        });

        btnPreviousCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (rowIdx > 0) {
                   Row row = STUDY_LIST.get(--rowIdx);
                   textDeckTitle.setText(row.getDeck().getDeckName());
                   textCardQuestionContent.setText(row.getCard().getCardQuestion());
                   textStudyProgress.setText("Card " + String.valueOf(rowIdx+1) + " / " + String.valueOf(STUDY_LIST.size()));
                   progressBar.setProgress(rowIdx+1);
               }
               else {
                   rowIdx = -1;
                   textCardQuestionContent.setText("You have reached the start of the cards.");
                   textStudyProgress.setText("Card " + String.valueOf(rowIdx+1) + " / " + String.valueOf(STUDY_LIST.size()));
                   progressBar.setProgress(rowIdx+1);
               }
           }
        });

        cardStudyFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyBack studyBack = new StudyBack(db);
                Bundle bundle = new Bundle();
                bundle.putInt("rowIdx", rowIdx);
                studyBack.setArguments(bundle);

                if ((rowIdx >= 0) && (rowIdx < STUDY_LIST.size())) {
                    studyBack.show(getFragmentManager(), "Answer");
                }
            }
        });

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
