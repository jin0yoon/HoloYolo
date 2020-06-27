package com.example.user.holoyolo;

import com.google.firebase.iid.FirebaseInstanceId;

public class Reply {
    String id = FirebaseInstanceId.getInstance().getId();
    public String contents;
    String nickname;

    public Reply() {
        //empty constructor needed
    }
    public Reply(String id){
        this.id=id;
    }

    public Reply(String id, String nickname, String contents){
        this.id=id;
        this.nickname=nickname;
        this.contents=contents;
    }

    public String getId(){
        return id;
    }
    public String getNickname(){
        return nickname;
    }
    public String getContents(){
        return contents;
    }
}
