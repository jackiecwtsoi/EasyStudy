package com.example.learning.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.learning.DeckEntity;
import com.example.learning.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class HomeSelfDeckAdapter extends RecyclerView.Adapter<HomeSelfDeckAdapter.VH>{
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    static class VH extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView cardNum;
        ImageView userProfile;
        ImageView deckCover;
        TextView userName;
        ImageView delete;

        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.home_deck_item_title);
            description = itemView.findViewById(R.id.home_deck_item_description);
            cardNum = itemView.findViewById(R.id.home_deck_item_card_num);
            userProfile = itemView.findViewById(R.id.home_deck_item_user_profile);
            userName = itemView.findViewById(R.id.home_deck_item_user_name);
            deckCover = itemView.findViewById(R.id.home_deck_item_img);
        }

    }
    private List<DeckEntity> dataList;
    private List<DeckEntity> dataListCopy = new ArrayList<>();
    private Context context;

    public HomeSelfDeckAdapter(List<DeckEntity> dataList, Context context) {
        this.dataList = dataList;
        this.dataListCopy.addAll(dataList);
        this.context = context;

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
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_deck_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        DeckEntity c = dataList.get(position);

        holder.title.setText(c.getDeckName());
        holder.description.setText(c.getDeckDescription());
        holder.cardNum.setText(Integer.toString(c.getCardNum()) + " cards");
        RequestOptions options = new RequestOptions().error(R.drawable.spring_showers).bitmapTransform(new RoundedCorners(30));
//        Glide.with(context).load(c.getCoverPath()).into(holder.deckCover);
        Uri img = Uri.fromFile(new File(c.getCoverPath()));
        try {
            Bitmap bitmap = BitmapFactory.decodeStream
                    (context.getContentResolver().openInputStream(img));
            holder.deckCover.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(c.getCoverPath());
        if (mOnItemClickLitener != null) {
            holder.deckCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });
        }
//        if(mOnDeleteItemClickLitener != null){
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    holder.swipeRevealLayout.close(false);
//                    mOnDeleteItemClickLitener.onItemClick(view, position);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }
}
