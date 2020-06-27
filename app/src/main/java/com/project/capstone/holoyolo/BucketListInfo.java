package com.example.user.holoyolo;

import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;

public class BucketListInfo {
    String id = FirebaseInstanceId.getInstance().getId();
    public String contents;
    String subject;
    String nickname;
    String seem;
    public String filePath;
    private String mKey;


    public BucketListInfo() {
        //empty constructor needed
    }

    public BucketListInfo(String id, String nickname, String subject, String contents){
        this.id=id;
        this.nickname=nickname;
        this.subject=subject;
        this.contents=contents;
    }

    public BucketListInfo(String id, String nickname, String subject, String contents, String seem, String filePath){
        this.id=id;
        this.nickname=nickname;
        this.subject=subject;
        this.contents=contents;
        this.seem=seem;
        this.filePath=filePath;
    }

    public String getId(){
        return id;
    }
    public String getSubject(){
        return subject;
    }
    public String getSeem(){
        return seem;
    }
    public String getFilePath(){
        return filePath;
    }
    public String getNickname(){
        return nickname;
    }
    public String getContents(){
        return contents;
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
