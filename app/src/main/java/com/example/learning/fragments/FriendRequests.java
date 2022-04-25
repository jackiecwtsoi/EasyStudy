package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.learning.FriendStatus;
import com.example.learning.MainActivity;
import com.example.learning.R;

import java.util.ArrayList;

public class FriendRequests extends Fragment {
    // define variables
    View rootView;
    Button btnAccept, btnReject;
    ImageView btnFriendRequestsBack;
    RecyclerView recyclerViewIncoming, recyclerViewOutgoing;
    SQLiteDatabase db;
    int userId;

    // define list of people who have sent friend request to the user
    ArrayList<FriendEntity> listIncomingFriendRequests = new ArrayList<>();
    // define list of people the user has sent friend request to
    ArrayList<FriendEntity> listOutgoingFriendRequests = new ArrayList<>();

    IncomingFriendRequestsAdapter adapterIncoming;
    OutgoingFriendRequestsAdapter adapterOutgoing;

    public FriendRequests(SQLiteDatabase db) {
        this.db = db;
    }

    public static FriendRequests newInstance(SQLiteDatabase db) {
        FriendRequests fragment = new FriendRequests(db);
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

        // inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        DbApi dbapi = new DbApi(this.db);

        // buttons
        btnAccept = rootView.findViewById(R.id.btnAccept);
        btnReject = rootView.findViewById(R.id.btnReject);
        btnFriendRequestsBack = rootView.findViewById(R.id.btnFriendRequestsBack);

        // recycler views
        recyclerViewIncoming = rootView.findViewById(R.id.recyclerViewIncomingFriendRequests);
        recyclerViewOutgoing = rootView.findViewById(R.id.recyclerViewOutgoingFriendRequests);

        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();

        // incoming friend requests
        listIncomingFriendRequests = dbapi.getIncomingFriendRequests(userId);
        adapterIncoming = new IncomingFriendRequestsAdapter(userId, listIncomingFriendRequests, db);
        recyclerViewIncoming.setAdapter(adapterIncoming);
        Context context = getActivity();
        recyclerViewIncoming.setLayoutManager(new LinearLayoutManager(context));

        adapterIncoming.setOnItemClickListener(new IncomingFriendRequestsAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(int position) {
                updateFriendStatus(position);
                removeFromIncomingRequests(position);
            }

            @Override
            public void onRejectClick(int position) {
                deleteFriend(position);
                removeFromIncomingRequests(position);
            }
        });

        // TODO: Outgoing friend requests???
//        // outgoing friend requests
//        listOutgoingFriendRequests = dbapi.getOutgoingFriendRequests(userId);
//        adapterOutgoing = new OutgoingFriendRequestsAdapter(listOutgoingFriendRequests);
//        recyclerViewOutgoing.setAdapter(adapterOutgoing);
//        recyclerViewOutgoing.setLayoutManager(new LinearLayoutManager(context));

        btnFriendRequestsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("change to friends list");
                Friends friendsFragment = new Friends(db);
                FragmentManager friendsFragmentManager = getFragmentManager();
                friendsFragmentManager.beginTransaction()
                        .replace(R.id.layoutFriendRequests, friendsFragment)
                        .addToBackStack("friend requests")
                        .commit();
            }
        });

        return rootView;
    }

    // helper function to call dbapi and update the friend row in the friend table
    public void updateFriendStatus(int position) {
        DbApi dbapi = new DbApi(this.db);
        FriendEntity friend = listIncomingFriendRequests.get(position);
        dbapi.updateFriendStatus(userId, friend, FriendStatus.FRIEND);
    }

    // helper function to call dbapi and delete the friend row in the friend table
    public void deleteFriend(int position) {
        DbApi dbapi = new DbApi(this.db);
        FriendEntity friend = listIncomingFriendRequests.get(position);
        dbapi.deleteFriend(userId, friend);
    }

    // helper function to remove the request item from the request list
    public void removeFromIncomingRequests(int position) {
        listIncomingFriendRequests.remove(position);
        adapterIncoming.notifyItemRemoved(position);
    }

}
