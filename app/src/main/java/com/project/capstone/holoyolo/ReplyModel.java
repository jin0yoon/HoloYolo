package com.example.user.holoyolo;
import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;

public class ReplyModel {

    String id = FirebaseInstanceId.getInstance().getId();
    public String contents;
    String nickname;
    String key;

    public ReplyModel(){

    }

    public ReplyModel(String id, String nickname, String contents){
        this.id = id;
        this.nickname = nickname;
        this.contents = contents;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setkey(String key2) {
        key = key2;
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
