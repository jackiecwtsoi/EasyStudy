package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.learning.DbApi;
import com.example.learning.DeckEntity;
import com.example.learning.FriendEntity;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;


public class Home extends Fragment {

    View rootView;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    Context context;
    SQLiteDatabase db;
    DbApi dbApi;
    int userId;
    ArrayList<DeckEntity> decks = new ArrayList<>();
    ArrayList<FriendEntity> friends = new ArrayList<>();
    ArrayList<DeckEntity> friendDecks = new ArrayList<>();

    //The parameters of user image in home page.
    private ImageView user_image;
    private ImageView btnFriends;
    private TextView userName;
    private String name;


    public Home(SQLiteDatabase db) {
        this.db = db;
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2, SQLiteDatabase db) {
        Home fragment = new Home(db);
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
            container.removeView(container.findViewById(R.id.recyclerViewFriends));
            container.removeView(container.findViewById(R.id.btnFriendRequests));
            container.removeView(container.findViewById(R.id.btnAddFriend));
            container.removeView(container.findViewById(R.id.btnFriendsBack));
            container.removeView(container.findViewById(R.id.textContacts));
            container.removeView(container.findViewById(R.id.btnProfileBack));
            container.removeView(container.findViewById(R.id.user_profile));
            container.removeView(container.findViewById(R.id.Edit_title));
            container.removeView(container.findViewById(R.id.profile_mail_label));
            container.removeView(container.findViewById(R.id.TextTextmail));
            container.removeView(container.findViewById(R.id.profile_name_label));
            container.removeView(container.findViewById(R.id.editTextTextPersonName));
            container.removeView(container.findViewById(R.id.profile_phone_label));
            container.removeView(container.findViewById(R.id.editTextTextPhone));
            container.removeView(container.findViewById(R.id.profile_password_label));
            container.removeView(container.findViewById(R.id.editTextTextPhone));
            container.removeView(container.findViewById(R.id.cacel_change_btn));
            container.removeView(container.findViewById(R.id.save_change_btn));
//            container.removeView(container.findViewById(R.id.touxiang));
        }
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        dbApi = new DbApi(db);
        context = getActivity();
        MainActivity mainActivity = (MainActivity) getActivity();
        userId = mainActivity.getLoginUserId();
        name = mainActivity.getUserName();
        getDeckList();
        userName = rootView.findViewById(R.id.home_user_name);
        userName.setText("Hi "+name + "!");
        recyclerView1 = rootView.findViewById(R.id.banner_recycler1);
        recyclerView3 = rootView.findViewById(R.id.banner_recycler3);
        initRecycler();
        user_image = rootView.findViewById(R.id.touxiang);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profile profileFragment = new Profile(db);
                System.out.println("fragment");
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.home_root_layout, profileFragment)
                        .commit();
            }
        });
        String userProfileURL = dbApi.queryUserProfileURL(userId);
        if (!userProfileURL.isEmpty()) {
            Picasso.get()
                    .load(userProfileURL)
                    .into(user_image);
        }

        // define variables
        //btnFriends = rootView.findViewById(R.id.btnFriends);
        btnFriends = rootView.findViewById(R.id.friend);

        // when the friends button is pressed, switch to a new fragment which contains the friends list
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends friendsFragment = new Friends(db);
                FragmentManager friendsFragmentManager = getFragmentManager();
                friendsFragmentManager.beginTransaction()
                        .replace(R.id.home_root_layout, friendsFragment)
                        .commit();
            }
        });

        return rootView;
    }

    private void initRecycler() {
        LinearLayoutManager ms = new LinearLayoutManager(context);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager ms2 = new LinearLayoutManager(context);
        ms2.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager ms3= new LinearLayoutManager(context);
        ms3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(ms);
        recyclerView3.setLayoutManager(ms3);
        HomeDeckAdapter adapter1 = new HomeDeckAdapter(friendDecks, context, friends);
        HomeSelfDeckAdapter adapter3 = new HomeSelfDeckAdapter(decks, context, name, dbApi.queryUserProfileURL(userId));
        adapter1.setOnItemClickLitener(new HomeDeckAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity main = (MainActivity) getActivity();
                main.changeToDeck();
                CardFragment fragment = new CardFragment(db, friendDecks.get(position));
                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutFolder, fragment)
                        .addToBackStack("tag13")
                        .commit();
                System.out.println(position);
            }
        });
        adapter3.setOnItemClickLitener(new HomeSelfDeckAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity main = (MainActivity) getActivity();
                main.changeToDeck();
                CardFragment fragment = new CardFragment(db, decks.get(position));
                FragmentManager studyFrontManager = getFragmentManager();
                studyFrontManager.beginTransaction()
                        .replace(R.id.layoutFolder, fragment)
                        .addToBackStack("tag13")
                        .commit();
                System.out.println(position);
            }
        });
        recyclerView1.setAdapter(adapter1);
        recyclerView3.setAdapter(adapter3);
    }

    private void getDeckList(){
        friends.clear();
        friends.addAll(dbApi.getConfirmedFriends(userId));
        friendDecks.clear();
        System.out.println("have frineds:" + Integer.toString(friends.size()));
        for (FriendEntity friend: friends){
            friendDecks.add(dbApi.getAllPublicDecks(friend.getFriendId()).get(0));
        }
        decks.clear();
        decks.addAll(dbApi.getAllDecks(userId));
        for(DeckEntity deck : friendDecks){
            int deckNum = dbApi.queryCard(deck.getDeckID(), deck.getFolderId(), deck.getUserId()).size();
            deck.setCardNum(deckNum);
        }
        for(DeckEntity deck : decks){
            int deckNum = dbApi.queryCard(deck.getDeckID(), deck.getFolderId(), deck.getUserId()).size();
            deck.setCardNum(deckNum);
        }
        System.out.println("home deck length:"+ Integer.toString(decks.size()));
    }

}
