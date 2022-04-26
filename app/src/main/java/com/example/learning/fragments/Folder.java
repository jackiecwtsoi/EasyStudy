package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.learning.DbApi;
import com.example.learning.FolderEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;

import java.util.ArrayList;
import java.util.List;

public class Folder extends Fragment {
    private String mParam1;
    private String mParam2;
    RecyclerView folder_viewer;
    FolderAdapter adapter;
    private View rootView;
    private SearchView searchView;
    private ImageView addFolder;
    private View addView;
    private SQLiteDatabase db;
    private DbApi dbApi;
    private ArrayList<FolderEntity> folders = new ArrayList<>();
    private int userId = -1;

    public Folder(SQLiteDatabase db) {
        this.db = db;
    }

    public static Folder newInstance(String param1, String param2, SQLiteDatabase db) {
        Folder fragment = new Folder(db);
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
        getFolderList();

        addView = inflater.inflate(R.layout.create_folder, container, false);
        addView.setBackgroundColor(Color.WHITE);
        folder_viewer = rootView.findViewById(R.id.foler_viewer);
        searchView = rootView.findViewById(R.id.folder_search);
        addFolder = rootView.findViewById(R.id.folder_add);
        Context context = getActivity();
        adapter = new FolderAdapter(folders);
        folder_viewer.setAdapter(adapter);
        adapter.setOnItemClickLitener(new FolderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("click the on item");
                Deck deck = new Deck(folders.get(position).getFolderID(), db);
                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutFolder, deck)
                        .addToBackStack("tag")
                        .commit();
                System.out.println(position);
            }
        });
        adapter.setDeleteOnItemClickLitener(new FolderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("click the on delete");

                dbApi.deleteFolder(userId, folders.get(position).getFolderID());
                getFolderList();
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
                final EditText description = addView.findViewById(R.id.create_folder_description);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbApi.insertFolder(title.getText().toString(), description.getText().toString(), userId);
                        dialog.dismiss();
                        getFolderList();
                        adapter.notifyDataSetChanged();
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
    private void getFolderList() {
        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();
        dbApi = new DbApi(db);
        folders.clear();
        folders.addAll(dbApi.queryFolder(userId));
        for (FolderEntity folder : folders){
            int deckNums = dbApi.queryDeck(folder.getFolderID(), userId).size();
            folder.setDeckNums(deckNums);
        }
    }
}
