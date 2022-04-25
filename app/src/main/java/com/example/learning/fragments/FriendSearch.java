package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.DbApi;
import com.example.learning.Difficulty;
import com.example.learning.FriendEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.Row;

import java.util.ArrayList;

public class FriendSearch extends DialogFragment {
    // define variables
    View rootView;
    EditText editFriendEmail;
    CardView cardFriendSearch;
    Button btnCancelFriendSearch, btnFriendSearch;

    SQLiteDatabase db;
    String friendEmail;
    int userId;

    public FriendSearch(SQLiteDatabase db) {
        this.db = db;
    }

    public static FriendSearch newInstance(SQLiteDatabase db) {
        FriendSearch fragment = new FriendSearch(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friend_search, container, false);
        editFriendEmail = rootView.findViewById(R.id.editFriendEmail);
        cardFriendSearch = rootView.findViewById(R.id.cardFriendSearch);
        btnCancelFriendSearch = rootView.findViewById(R.id.btnCancelFriendSearch);
        btnFriendSearch = rootView.findViewById(R.id.btnFriendSearch);

        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();

        // tap on the popup fragment returns user back to the friends list
        cardFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // dismiss this search fragment if user clicks "Cancel"
        btnCancelFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // when the "Search" button is clicked, search the database using the email
        btnFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendEmail = editFriendEmail.getText().toString();

                // check if nothing has been entered into the field
                if (friendEmail.matches("")) {
                    Toast.makeText(getContext(), "Please enter your friend's email.", Toast.LENGTH_LONG).show();
                } else {
                    searchFriendByEmail(friendEmail);
                }
            }
        });

        return rootView;
    }

    private void searchFriendByEmail(String email) {
        DbApi dbapi = new DbApi(this.db);
        int userIdFromSearch = dbapi.queryUserByEmail(email);
        if (userIdFromSearch == -1) { // if user is not found
            Toast.makeText(getContext(), "No user is found!", Toast.LENGTH_LONG).show();
            editFriendEmail.getText().clear(); // clear the edit text field
        }
        else { // if user is found, then go to another fragment showing the user info and add friend option
            dismiss();
            changeToSearchedUserInfo(userIdFromSearch);
        }
    }

    private boolean isConfirmedFriend(int userIdFromSearch) { // check if the searched user is already a friend
        DbApi dbapi = new DbApi(this.db);
        ArrayList<FriendEntity> confirmedFriends = dbapi.getConfirmedFriends(this.userId);
        for (FriendEntity friend : confirmedFriends) {
            if (friend.getFriendId() == userIdFromSearch) return true;
        }
        return false;
    }

    private void changeToSearchedUserInfo(int userIdFromSearch) {
        FriendSearchResult friendSearchResultFragment = new FriendSearchResult(db);
        Bundle bundle = new Bundle();
        boolean IS_FRIEND = false;

        // check if searched user is already a friend
        if (isConfirmedFriend(userIdFromSearch)) { // if yes, set isFriend = true
            IS_FRIEND = true;
        }

        bundle.putBoolean("IS_FRIEND", IS_FRIEND);
        bundle.putInt("userIdFromSearch", userIdFromSearch);
        friendSearchResultFragment.setArguments(bundle);

        FragmentManager friendSearchResultFragmentManager = getFragmentManager();
        friendSearchResultFragmentManager.beginTransaction()
                .replace(R.id.layoutFriends, friendSearchResultFragment)
                .commit();
    }
}
