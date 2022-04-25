package com.example.learning.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learning.AddCardActivity;
import com.example.learning.Card;
import com.example.learning.DbApi;
import com.example.learning.DeckEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.customizeview.CircleProgressView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

public class CardFragment extends Fragment {
    private String mParam1;
    private String mParam2;
    SQLiteDatabase db;
    View rootView;
    private ScheduledFuture<?> scheduledFuture;
    private float completion = (float)0.9;
    DbApi dbApi;
    DeckEntity deck;
    ArrayList<Card> cards = new ArrayList<>();
    TextView cardNum;
    TextView decktile;
    TextView userNameText;
    ImageView deckCover;


    public CardFragment(SQLiteDatabase db, DeckEntity deck) {
        // Required empty public constructor
        this.db = db;
        this.deck = deck;
    }
    private CircleProgressView cnp_citcleNumberProgress;

    /** 指定给进度条的进程 */
    private float progress;
    protected static final int WHAT_INCREASE = 1;
    TextView addCard;
    TextView changeToStudy;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress = progress + (float) 0.05;
            cnp_citcleNumberProgress.setProgress(progress);
            handler.sendEmptyMessageDelayed(WHAT_INCREASE, 30);
            if (progress >= deck.getCompletion() + 0.1) {
                handler.removeMessages(WHAT_INCREASE);
            }
        }

    };

    public static CardFragment newInstance(String param1, String param2, SQLiteDatabase db, DeckEntity deck) {
        CardFragment fragment = new CardFragment(db, deck);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_card, container, false);
        dbApi = new DbApi(db);

        qeuryCard();
        cnp_citcleNumberProgress = rootView.findViewById(R.id.card_deck_progress);
        increase();
        deckCover = rootView.findViewById(R.id.deck_cover);
        String coverPath = deck.getCoverPath();
        Uri img = Uri.fromFile(new File(coverPath));
        try {
            Bitmap bitmap = BitmapFactory.decodeStream
                    (getActivity().getContentResolver().openInputStream(img));
            deckCover.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addCard = rootView.findViewById(R.id.card_add_card);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), AddCardActivity.class);
                intent.putExtra("deck_id", deck.getDeckID());
                intent.putExtra("user_id", deck.getUserId());
                intent.putExtra("folder_id", deck.getFolderId());
                startActivity(intent);

            }
        });
        cardNum = rootView.findViewById(R.id.card_card_num);
        cardNum.setText(Integer.toString(cards.size())+ " cards |");
        decktile = rootView.findViewById(R.id.card_deck_title);
        decktile.setText(deck.getDeckName());
        changeToStudy = rootView.findViewById(R.id.card_to_study);
        changeToStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                main.changeToStudy(deck);
            }
        });
        userNameText = rootView.findViewById(R.id.card_user_name);
        userNameText.setText(dbApi.queryUserName(deck.getUserId()));
        return rootView;
    }

    private void increase() {
        progress = 0;
        handler.removeMessages(WHAT_INCREASE);
        handler.sendEmptyMessage(WHAT_INCREASE);
    }

    @Override
    public void onResume() {
        super.onResume();
        qeuryCard();
        cardNum.setText(Integer.toString(cards.size())+ " cards |");
    }

    private void qeuryCard(){
        cards.clear();
        System.out.println(deck.getDeckName());
        cards.addAll(dbApi.queryCard(deck.getDeckID(), deck.getFolderId(), deck.getUserId()));
    }
}
