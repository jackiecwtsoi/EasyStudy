package com.example.learning.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning.FriendEntity;
import com.example.learning.R;

import java.util.ArrayList;

public class OutgoingFriendRequestsAdapter extends RecyclerView.Adapter<OutgoingFriendRequestsAdapter.VH> {
    private ArrayList<FriendEntity> dataList;

    static class VH extends RecyclerView.ViewHolder {
        TextView textOutgoingFriendName;

        public VH(@NonNull View itemView) {
            super(itemView);
            textOutgoingFriendName = itemView.findViewById(R.id.outgoing_friend_name);
        }
    }

    public OutgoingFriendRequestsAdapter(ArrayList<FriendEntity> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public OutgoingFriendRequestsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outgoing_friends_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OutgoingFriendRequestsAdapter.VH holder, int position) {
        FriendEntity friend = dataList.get(position);
        holder.textOutgoingFriendName.setText(friend.getFriendName());
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
}
