package com.example.user.holoyolo;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;

public class Upload {
    private String mKey;
    public String id;
    public String name;
    public String contents;
    public double latitude;
    public double longtitude;
    public String category;
    String nickname;
    String placename;
    public Uri Imgurl;
    public String seem;
    public String filePath;
    public int starCount;
    String subject;
    public String title;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, double latitude, double longtitude, String contents){
        this.name = name;
        this.latitude=latitude;
        this.longtitude=longtitude;
        this.contents=contents;
    }

    public Upload(String id, String nickname, String subject, String contents){
        this.id=id;
        this.nickname=nickname;
        this.subject=subject;
        this.contents=contents;
    }

    public Upload(String id, String nickname, String subject, String contents, String seem, String filePath){
        this.id=id;
        this.nickname=nickname;
        this.subject=subject;
        this.contents=contents;
        this.seem=seem;
        this.filePath=filePath;
    }


    public Upload(String nickname, String id, String category, String name, String seem, String placename, double latitude, double longtitude, String contents, String filePath, int starCount){
        this.nickname=nickname;
        this.id =id;
        this.category=category;
        this.name=name;
        this.seem=seem;
        this.placename=placename;
        this.latitude=latitude;
        this.longtitude=longtitude;
        this.contents=contents;
        this.filePath=filePath;
        this.starCount=starCount;
    }

    public Upload(String nickname, String title){
        this.nickname = nickname;
        this.title = title;
    }

    public String getUserId(){
        return id;
    }

    public int getStarCount(){
        return starCount;
    }

    public String getSeem(){
        return seem;
    }

    public String getNickname(){
        return nickname;
    }

    public String getPlacename(){
        return placename;
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

    public double getLatitude(){
        return latitude;
    }

    public double getLongtitude(){
        return longtitude;
    }

    public String getFilePath(){
        return filePath;
    }
    public void setStarCount(int starCount2){
        starCount = starCount2;
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