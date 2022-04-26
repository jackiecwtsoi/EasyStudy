package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.learning.DbApi;
import com.example.learning.DeckEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;

import java.util.ArrayList;
import java.util.List;

public class Deck extends Fragment {
    RecyclerView folder_viewer;
    DeckAdapter adapter;
    private View rootView;
    private SearchView searchView;
    private ImageView addFolder;
    private View addView;
    List<DeckEntity> deckList = new ArrayList<>();
    private int folder_id;
    SQLiteDatabase db;
    DbApi dbApi;
    int userID;
    public Deck(int folder, SQLiteDatabase db) {
        this.folder_id = folder;
        this.db = db;
    }

    public static Deck newInstance(String param1, String param2, int folder_id, SQLiteDatabase db) {
        Deck fragment = new Deck(folder_id, db);
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

        rootView = inflater.inflate(R.layout.fragment_folder, container, false);
        MainActivity main = (MainActivity) getActivity();
        userID = main.getLoginUserId();
        dbApi = new DbApi(db);
        getDeckList();
        addView = inflater.inflate(R.layout.create_folder, container, false);
        addView.setBackgroundColor(Color.WHITE);
        folder_viewer = rootView.findViewById(R.id.foler_viewer);
        searchView = rootView.findViewById(R.id.folder_search);
        addFolder = rootView.findViewById(R.id.folder_add);
        addFolder.setVisibility(View.GONE);


        Context context = getActivity();
        adapter = new DeckAdapter(deckList);
        folder_viewer.setAdapter(adapter);
        adapter.setOnItemClickLitener(new DeckAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                MainActivity main = (MainActivity) getActivity();
//                main.changeToStudy();
                CardFragment fragment = new CardFragment(db, deckList.get(position));
                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutFolder, fragment)
                        .addToBackStack("tag13")
                        .commit();
                System.out.println(position);
            }
        });
        adapter.setDeleteOnItemClickLitener(new DeckAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("click the on delete");
                dbApi.deleteDeck(userID, deckList.get(position).getFolderId(), deckList.get(position).getDeckID());
                getDeckList();
                adapter.notifyDataSetChanged();
            }
        });
        folder_viewer.setLayoutManager(new LinearLayoutManager(context));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return true;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        final AlertDialog dialog = builder.create();
        // dialog.setTitle("Add folder");

        dialog.setView(addView);
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                TextView cancel = addView.findViewById(R.id.add_folder_cancel);
                TextView save = addView.findViewById(R.id.add_folder_save);
                final EditText title = addView.findViewById(R.id.create_folder_input);
                EditText description = addView.findViewById(R.id.create_folder_description);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return rootView;
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }
    private void getDeckList(){
        deckList.clear();
        deckList.addAll(dbApi.queryDeck(folder_id, userID));
        for(DeckEntity deck : deckList){
            int deckNum = dbApi.queryCard(deck.getDeckID(), folder_id, userID).size();
            deck.setCardNum(deckNum);
        }
    }

}
