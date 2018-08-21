package com.studysemina.snsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studysemina.snsapp.R;
import com.studysemina.snsapp.model.ChatData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<ChatData> mList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference commentDelete;

    public ChatAdapter( List<ChatData> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChatData chatData = mList.get(position);

        holder.item_chatNickTv.setText(chatData.getNickname());
        holder.item_chatCommentTv.setText(chatData.getComment());
        holder.item_chatTimeTv.setText(getDate(chatData.getTimestamp()));
        holder.item_chatImg.setImageResource(R.drawable.ic_wi_alien);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(List<ChatData> chatData) {
        mList.addAll(chatData);
    }
    public void clear() {
        mList.clear();
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

    private String getDate (long milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh-mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());

    }
}
