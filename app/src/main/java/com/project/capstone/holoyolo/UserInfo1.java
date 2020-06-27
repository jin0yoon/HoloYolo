package com.example.user.holoyolo;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;

public class UserInfo1{
    public String nickname;
    public String id;
    public String email;

    public UserInfo1() {
        //empty constructor needed
    }

    public UserInfo1(String email, String nickname){
        this.email=email;
        this.nickname=nickname;
    }

    public UserInfo1(String email, String nickname, String id){
        this.email=email;
        this.nickname=nickname;
        this.id =id;
    }

    public String getNickname(){
        return nickname;
    }

    public String getUserId(){
        return id;
    }

}
