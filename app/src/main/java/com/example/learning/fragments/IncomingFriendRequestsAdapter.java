package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning.DbApi;
import com.example.learning.FriendEntity;
import com.example.learning.FriendStatus;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IncomingFriendRequestsAdapter extends RecyclerView.Adapter<IncomingFriendRequestsAdapter.VH> {
    private SQLiteDatabase db;
    private ArrayList<FriendEntity> dataList;
    private int userId;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView textIncomingFriendName;
        FloatingActionButton btnAccept, btnReject;
        ImageView imgFriendPicture;

        public VH(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textIncomingFriendName = itemView.findViewById(R.id.incoming_friend_name);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            imgFriendPicture = itemView.findViewById(R.id.incoming_friend_picture);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAcceptClick(position);
                        }
                    }
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRejectClick(position);
                        }
                    }
                }
            });

        }
    }

    public IncomingFriendRequestsAdapter(int userId, ArrayList<FriendEntity> dataList, SQLiteDatabase db) {
        this.userId = userId;
        this.dataList = dataList;
        this.db = db;
    }

    @NonNull
    @Override
    public IncomingFriendRequestsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incoming_friends_item_layout, parent, false);
        IncomingFriendRequestsAdapter.VH vh = new IncomingFriendRequestsAdapter.VH(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingFriendRequestsAdapter.VH holder, int position) {
        FriendEntity friend = dataList.get(position);
        holder.textIncomingFriendName.setText(friend.getFriendName());
        if (!friend.getFriendPicture().isEmpty()) {
            Picasso.get().load(friend.getFriendPicture()).into(holder.imgFriendPicture);
        }
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
}
