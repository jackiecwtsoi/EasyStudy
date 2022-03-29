package com.example.learning.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.VH>{
    static class VH extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;

         VH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.folder_name);
            tv2 = itemView.findViewById(R.id.folder_pro);
        }
    }
    private List<Character> dataList;

    public FolderAdapter(List<Character> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Character c = dataList.get(position);
        holder.tv1.setText(c.toString());
        holder.tv2.setText(String.valueOf(Integer.valueOf(c)));
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
