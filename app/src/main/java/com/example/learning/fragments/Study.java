package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.DbApi;
import com.example.learning.DeckEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.StudyType;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Study#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Study extends Fragment {
    // define variables
    View rootView;
    TextView btnReviewSelectedDeck, btnReviewAllCards, btnReviewTodayCards;
    RecyclerView recyclerViewTasksToday;
    TextView textSelectedDeckName;
    SQLiteDatabase db;
    int userId;
    Calendar calendar = Calendar.getInstance();
    String dayOfWeek;

    StudyType STUDY_TYPE = StudyType.ALL_CARDS; // indicates whether we study ALL CARDS from the database
    DeckEntity selected_deck;

    ArrayList<DeckEntity> listTasksToday = new ArrayList<>();
    TasksTodayAdapter adapter;

    public Study(SQLiteDatabase db) {
        this.db = db;
    }

    public static Study newInstance(SQLiteDatabase db) {
        Study fragment = new Study(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeView(container.findViewById(R.id.cardStudyDoneOverall));
            container.removeView(container.findViewById(R.id.btnReviewAgain));
            container.removeView(container.findViewById(R.id.btnReturnStudyOverview));
        }
//        if (rootView != null) {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (parent != null) {
//                parent.removeView(rootView);
//            }
//        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_study, container, false);
        DbApi dbapi = new DbApi(this.db);
        btnReviewSelectedDeck = rootView.findViewById(R.id.btnReviewSelectedDeck);
        btnReviewTodayCards = rootView.findViewById(R.id.btnReviewTodayCards);
        btnReviewAllCards = rootView.findViewById(R.id.btnReviewAllCards);
        textSelectedDeckName = rootView.findViewById(R.id.textSelectedDeckName);

        // recycler view
        recyclerViewTasksToday = rootView.findViewById(R.id.recyclerViewTasksToday);
        MainActivity main = (MainActivity) getActivity();
        selected_deck = main.getSelectedDeck();
        userId = main.getLoginUserId();
        dayOfWeek = dbapi.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        listTasksToday = dbapi.getDecksForReminder(userId, dayOfWeek);
        adapter = new TasksTodayAdapter(listTasksToday);
        recyclerViewTasksToday.setAdapter(adapter);
        Context context = getActivity();
        recyclerViewTasksToday.setLayoutManager(new LinearLayoutManager(context));

        // when no cards to study for today, hide the "Review Today's Cards" button
        if (adapter.getItemCount() == 0) { // if recycler view adapter has no card in the list
            btnReviewTodayCards.setVisibility(View.INVISIBLE);
        }

        if (selected_deck == null) { // if no deck has been selected yet
            textSelectedDeckName.setText("No selected deck yet");
            btnReviewSelectedDeck.setText("Select A Deck");
        }
        else { // if deck has been selected
            textSelectedDeckName.setText(selected_deck.getDeckName());
        }

        // when the "Review Selected Deck" button is clicked, we only show the unfinished cards
        btnReviewSelectedDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_deck == null) { // if no selected deck yet, then the button goes to Folder tab
                    MainActivity main = (MainActivity) getActivity();
                    main.changeToFolder(-1);
                }
                else { // if deck is selected, then the button goes to StudyFront fragment for that deck
                    StudyFront studyFront = new StudyFront(db);

                    Bundle bundle = new Bundle();
                    STUDY_TYPE = StudyType.SELECTED_DECK; // since we do not want to study ALL CARDS from the database
                    bundle.putSerializable("STUDY_TYPE", STUDY_TYPE);
                    bundle.putSerializable("selected_deck", selected_deck);
                    studyFront.setArguments(bundle);

                    FragmentManager studyFrontManager = getFragmentManager();
                    studyFrontManager.beginTransaction()
                            .replace(R.id.layoutStudy, studyFront)
                            .commit();
                }
            }
        });

        // when the "Review Today's Cards" button is clicked, we show the decks that need to be REMINDED TODAY
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