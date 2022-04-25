package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning.DbApi;
import com.example.learning.FriendEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;

import java.util.ArrayList;

public class Friends extends Fragment {
    // define variables
    View rootView;
    ImageView btnAddFriend, btnFriendRequests, btnFriendsBack;
    RecyclerView recyclerViewFriends;
    SQLiteDatabase db;
    int userId;

    ArrayList<FriendEntity> listFriends = new ArrayList<>();
    FriendsAdapter adapter;

    public Friends(SQLiteDatabase db) {
        this.db = db;
    }

    public static Friends newInstance(SQLiteDatabase db) {
        Friends fragment = new Friends(db);
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
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        DbApi dbapi = new DbApi(this.db);
        btnAddFriend = rootView.findViewById(R.id.btnAddFriend);
        btnFriendRequests = rootView.findViewById(R.id.btnFriendRequests);
        btnFriendsBack = rootView.findViewById(R.id.btnFriendsBack);

        // recycler view
        recyclerViewFriends = rootView.findViewById(R.id.recyclerViewFriends);
        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();
        listFriends = dbapi.getConfirmedFriends(userId);
        adapter = new FriendsAdapter(userId, listFriends, db);
        recyclerViewFriends.setAdapter(adapter);
        Context context = getActivity();
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(context));

        adapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onFolderClick(int position) {
                changeToFriendFolder(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteFriend(position);
                removeFromIncomingRequests(position);
            }
        });

        // when the "Add Friend" button is clicked, switch to the add friend fragment
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendSearch friendSearchFragment = new FriendSearch(db);
                friendSearchFragment.show(getFragmentManager(), "Friend Search");
            }
        });

        // when the "Friend Requests" button is clicked, switch to the friend requests fragment
        btnFriendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendRequests friendRequestsFragment = new FriendRequests(db);
                FragmentManager friendRequestsFragmentManager = getFragmentManager();
                friendRequestsFragmentManager.beginTransaction()
                        .replace(R.id.layoutFriends, friendRequestsFragment)
                        .addToBackStack("requests")
                        .commit();
            }
        });

        // when the back button is clicked, return to home
        btnFriendsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("change to home");
                Home homeFragment = new Home(db);
                FragmentManager homeFragmentManager = getFragmentManager();
                homeFragmentManager.beginTransaction()
                        .replace(R.id.layoutFriends, homeFragment)
                        .addToBackStack("friends")
                        .commit();
            }
        });

        return rootView;
    }

    // helper function to call dbapi and delete the friend row in the friend table
    public void deleteFriend(int position) {
        DbApi dbapi = new DbApi(this.db);
        FriendEntity friend = listFriends.get(position);
        dbapi.deleteFriend(userId, friend.getFriendId());
        dbapi.deleteFriend(friend.getFriendId(), userId); // delete the duplicate row too
    }

    // helper function to remove the request item from the request list
    public void removeFromIncomingRequests(int position) {
        listFriends.remove(position);
        adapter.notifyItemRemoved(position);
    }

    // helper function to call MainActivity to pass friendId data to Folder fragment
    public void changeToFriendFolder(int position) {
        int selectedFriendId = listFriends.get(position).getFriendId();
        MainActivity main = (MainActivity) getActivity();
        main.changeToFolder(selectedFriendId);
    }
}
