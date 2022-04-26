package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning.DeckEntity;
import com.example.learning.FriendEntity;
import com.example.learning.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.VH> {
    private ArrayList<FriendEntity> dataList;
    private OnItemClickListener mListener;
    private SQLiteDatabase db;
    private int userId;

    public interface OnItemClickListener {
        void onFolderClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView textFriendName;
        ImageView btnViewFriendDecks, btnDeleteFriend;
        ImageView imgFriendPicture;

        public VH(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textFriendName = itemView.findViewById(R.id.friend_name);
            btnViewFriendDecks = itemView.findViewById(R.id.btnViewFriendDecks);
            btnDeleteFriend = itemView.findViewById(R.id.btnDeleteFriend);
            imgFriendPicture = itemView.findViewById(R.id.friend_picture);

            btnViewFriendDecks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFolderClick(position);
                        }
                    }
                }
            });

            btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public FriendsAdapter(int userId, ArrayList<FriendEntity> dataList, SQLiteDatabase db) {
        this.userId = userId;
        this.dataList = dataList;
        this.db = db;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_item_layout, parent, false);
        FriendsAdapter.VH vh = new FriendsAdapter.VH(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        FriendEntity friend = dataList.get(position);
        holder.textFriendName.setText(friend.getFriendName());
        if (!friend.getFriendPicture().isEmpty()) {
            Picasso.get().load(friend.getFriendPicture()).into(holder.imgFriendPicture);
        }
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
}
