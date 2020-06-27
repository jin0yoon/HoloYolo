package com.example.user.holoyolo;
import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;

public class FollowingData4 {
    String nickname;
    String key;
    String id = FirebaseInstanceId.getInstance().getId();

    public FollowingData4(){
    }

    public FollowingData4(String id, String nickname){
        this.id = id;
        this.nickname = nickname;
    }

    public String getId(){
        return id;
    }
    public String getNickname(){
        return nickname;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setkey(String key2) {
        key = key2;
    }
}
