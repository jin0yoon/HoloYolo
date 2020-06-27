package com.example.user.holoyolo;


import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;


public class BucketItem {
    private String mKey;
    String id = FirebaseInstanceId.getInstance().getId();
    public String nickname;
    public String title;

    public BucketItem() {
        //empty constructor needed
    }
    public BucketItem(String id){
        this.id=id;
    }

    public BucketItem(String id, String nickname, String title){
        this.id=id;
        this.nickname=nickname;
        this.title=title;
    }

    public String getId(){
        return id;
    }
    public String getNickname(){
        return nickname;
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

