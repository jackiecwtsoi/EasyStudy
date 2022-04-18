package com.example.learning.fragments;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning.DeckEntity;
import com.example.learning.R;

import java.util.ArrayList;

public class TasksTodayAdapter extends RecyclerView.Adapter<TasksTodayAdapter.VH> {
    static class VH extends RecyclerView.ViewHolder {
        TextView textDeckName;

        public VH(@NonNull View itemView) {
            super(itemView);
            textDeckName = itemView.findViewById(R.id.deck_name);
        }
    }
    private ArrayList<DeckEntity> dataList;

    public TasksTodayAdapter(ArrayList<DeckEntity> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public TasksTodayAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.study_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksTodayAdapter.VH holder, int position) {
        DeckEntity deck = dataList.get(position);
        holder.textDeckName.setText(deck.getDeckName());
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
}
