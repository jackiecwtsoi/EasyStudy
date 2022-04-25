package com.example.learning.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.learning.FolderEntity;
import com.example.learning.R;

import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.VH>{
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    static class VH extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView decktv;
        LinearLayout item;
        public SwipeRevealLayout swipeRevealLayout;
        ImageView delete;
         VH(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.folder_item);
            tv1 = itemView.findViewById(R.id.item_folder_title);
            tv2 = itemView.findViewById(R.id.item_folder_description);
            decktv = itemView.findViewById(R.id.folder_item_deck_num);
            swipeRevealLayout = itemView.findViewById(R.id.folder_swipereval);
            delete = itemView.findViewById(R.id.forlder_item_delete);
//            tv1 = itemView.findViewById(R.id.folder_name);
//            tv2 = itemView.findViewById(R.id.folder_pro);
            tv2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv2.getPaint().setAntiAlias(true);
         }

    }
    private List<FolderEntity> dataList;
    private List<FolderEntity> dataListCopy = new ArrayList<>();
    public FolderAdapter(List<FolderEntity> dataList) {
        this.dataList = dataList;
        this.dataListCopy.addAll(dataList);
    }

    private OnItemClickLitener   mOnItemClickLitener;
    private OnItemClickLitener   mOnDeleteItemClickLitener;

    //设置回调接口
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    public void setDeleteOnItemClickLitener(OnItemClickLitener mOnDeleteItemClickLitener){
        this.mOnDeleteItemClickLitener = mOnDeleteItemClickLitener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        FolderEntity c = dataList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(c.getFolderID()));
        holder.tv1.setText(c.getFolderName());
        holder.tv2.setText(c.getFolderDescription());
        holder.decktv.setText(Integer.toString(c.getDeckNums()) + " decks");
        if (mOnItemClickLitener != null) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });
        }
        if(mOnDeleteItemClickLitener != null){
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.swipeRevealLayout.close(false);
                    mOnDeleteItemClickLitener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void filter(String text) {
        dataList.clear();
        if(text.isEmpty()){
            dataList.addAll(dataListCopy);
        } else{
            text = text.toLowerCase();
            for(FolderEntity item: dataListCopy){
                if(item.getFolderName().toLowerCase().contains(text) || item.getFolderDescription().toLowerCase().contains(text)){
                    dataList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }
}
