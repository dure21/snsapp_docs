package com.studysemina.snsapp.model;


import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Date;

@IgnoreExtraProperties
public class ChatData {

    private String userId;
    private String nickname;
    private String comment;
    private long timestamp;

    public ChatData() {
    }



    public ChatData(String userId, String nickname, String comment, long timestamp) {
        this.userId = userId;
        this.nickname = nickname;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
