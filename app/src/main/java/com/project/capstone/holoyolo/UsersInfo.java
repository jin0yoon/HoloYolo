package com.example.user.holoyolo;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;

public class UsersInfo implements UserInfo {
    public String nickname;
    public String id;
    public String email;

    public UsersInfo() {
        //empty constructor needed
    }

    public UsersInfo(String email, String nickname){
        this.email=email;
        this.nickname=nickname;
    }

    public UsersInfo(String email, String nickname, String id){
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

    @Override
    public String getUid() {
        return null;
    }

    @Override
    public String getProviderId() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return null;
    }

    public String getEmail(){
        return email;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }
}
