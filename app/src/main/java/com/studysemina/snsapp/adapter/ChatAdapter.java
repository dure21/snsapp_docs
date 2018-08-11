package com.studysemina.snsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studysemina.snsapp.R;
import com.studysemina.snsapp.model.ChatData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatData> mItems;

    public ChatAdapter( ArrayList<ChatData> item) {
        mItems = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_chatNickTv.setText("");
        holder.item_chatCommentTv.setText("");
        holder.item_chatTimeTv.setText("");
        holder.item_chatImg.setImageResource(R.drawable.ic_wi_alien);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_chatNickTv) TextView item_chatNickTv;
        @BindView(R.id.item_chatCommentTv) TextView item_chatCommentTv;
        @BindView(R.id.item_chatTimeTv) TextView item_chatTimeTv;
        @BindView(R.id.item_chatImg) ImageView item_chatImg;

        ViewHolder(View viewItem) {
            super(viewItem);
            ButterKnife.bind(this,viewItem);
        }
    }
}
