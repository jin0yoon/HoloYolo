package com.example.user.holoyolo;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;

public class OtherBucketItem {
    private String mKey;
    public String id;
    public String name;
    public String contents;
    public String category;
    String nickname;

    public Uri Imgurl;
    public String seem;
    public String filePath;
    String subject;
    public String title;

    public OtherBucketItem() {
        //empty constructor needed
    }

    public OtherBucketItem(String id, String nickname, String subject, String contents){
        this.id=id;
        this.nickname=nickname;
        this.subject=subject;
        this.contents=contents;
    }

    public OtherBucketItem(String id, String nickname, String subject, String contents, String seem, String filePath){
        this.id=id;
        this.nickname=nickname;
        this.subject=subject;
        this.contents=contents;
        this.seem=seem;
        this.filePath=filePath;
    }

    public OtherBucketItem(String nickname, String title){
        this.nickname = nickname;
        this.title = title;
    }

    public String getUserId(){
        return id;
    }

    public String getSeem(){
        return seem;
    }

    public String getNickname(){
        return nickname;
    }

    public String getName(){
        return name;
    }

    public String getContents(){
        return contents;
    }

    public String getCategory(){
        return category;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getSubject(){
        return subject;
    }

    public String getTitle(){
        return title;
    }
    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setkey(String key) {
        mKey = key;
    }
}