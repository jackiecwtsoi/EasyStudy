package com.example.learning.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learning.DbApi;
import com.example.learning.Difficulty;
import com.example.learning.FriendStatus;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.Row;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendSearchResult extends Fragment {
    // define variables
    View rootView;
    Button btnAddSearchedFriend;
    TextView textSearchedFriendPublicNumber, textSearchedFriendPresentNumber;
    TextView textSearchedFriendName, textSearchedFriendEmail;
    ImageView imgSearchedFriendPicture;

    SQLiteDatabase db;
    int userIdFromSearch;
    Boolean IS_FRIEND;

    public FriendSearchResult(SQLiteDatabase db) { this.db = db; }

    public static FriendSearchResult newInstance(SQLiteDatabase db) {
        FriendSearchResult fragment = new FriendSearchResult(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            IS_FRIEND = bundle.getBoolean("IS_FRIEND");
            userIdFromSearch = bundle.getInt("userIdFromSearch");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        rootView = inflater.inflate(R.layout.fragment_friend_search_result, container, false);
        btnAddSearchedFriend = rootView.findViewById(R.id.btnAddSearchedFriend);
        textSearchedFriendPublicNumber = rootView.findViewById(R.id.textSearchedFriendPublicNumber);
        textSearchedFriendPresentNumber = rootView.findViewById(R.id.textSearchedFriendPresentNumber);
        textSearchedFriendName = rootView.findViewById(R.id.textSearchedFriendName);
        textSearchedFriendEmail = rootView.findViewById(R.id.textSearchedFriendEmail);
        imgSearchedFriendPicture = rootView.findViewById(R.id.imgSearchedFriendPicture);

        DbApi dbapi = new DbApi(this.db);
        ArrayList<String> searchedUserInfo = dbapi.getUserInfo(userIdFromSearch); // get array that displays selected user information-
        textSearchedFriendName.setText(searchedUserInfo.get(1));
        textSearchedFriendEmail.setText(searchedUserInfo.get(0));
        textSearchedFriendPublicNumber.setText(Integer.toString(dbapi.getPublicDecks(userIdFromSearch).size()));
        textSearchedFriendPresentNumber.setText(Integer.toString(dbapi.getPresentdays(userIdFromSearch)));

        // update profile picture
        String searchedFriendPictureURL = dbapi.queryUserProfileURL(userIdFromSearch);
        if (!searchedFriendPictureURL.isEmpty()) {
            Picasso.get()
                    .load(searchedFriendPictureURL)
                    .into(imgSearchedFriendPicture);
        }

        // if the searched user is already a friend, then we do not display the "Add Friend" button
        if (IS_FRIEND) {
            btnAddSearchedFriend.setVisibility(View.INVISIBLE);
        }

        // when the "Add Friend" button is clicked, update the 'friends' database and return to friends list
        btnAddSearchedFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFriend(); // push new row to the 'friends' database

                // return to friends list
                Friends friendsFragment = new Friends(db);
                FragmentManager friendsFragmentManager = getFragmentManager();
                friendsFragmentManager.beginTransaction()
                        .replace(R.id.layoutFriendSearchResult, friendsFragment)
                        .commit();
            }
        });

        return rootView;
    }

    // helper function to update the 'friends' database
    private void requestFriend() {
        DbApi dbapi = new DbApi(this.db);
        MainActivity main = (MainActivity) getActivity();
        int userId = main.getLoginUserId();
        dbapi.sendFriendRequest(userId, this.userIdFromSearch);
    }
}
