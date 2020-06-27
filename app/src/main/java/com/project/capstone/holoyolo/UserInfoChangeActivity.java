package com.example.user.holoyolo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInfoChangeActivity extends AppCompatActivity{
    private TextView textemail;
    private EditText editnickname;
    private Button btnsaveinfo;

    final String id = FirebaseInstanceId.getInstance().getId();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("MyPage").child(id).child("닉네임");

    private String emailget;
    private String nicknameoriginal;

    Map<String, Object> userNicknameUpdates = new HashMap<>();

    //닉네임 등록 영역
    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

            if(editnickname.getText().toString().equals(nicknameoriginal)){
                Intent intent = new Intent(UserInfoChangeActivity.this, MainActivity.class);
                startActivity(intent);
            }
            if(!editnickname.getText().toString().equals(nicknameoriginal)){
                while (child.hasNext()) {//마찬가지로 중복 유무 확인
                    if (editnickname.getText().toString().equals(child.next().getKey())) {
                        Toast.makeText(getApplicationContext(), "존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                        mDatabase.removeEventListener(this);
                        return;
                    }
                }
            }
            uploadUserInfo();
            Intent intent = new Intent(UserInfoChangeActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        textemail = (TextView) findViewById(R.id.textemail);
        editnickname = (EditText) findViewById(R.id.editnickname);
        btnsaveinfo = (Button) findViewById(R.id.btnsaveinfo);

        emailget = user.getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("닉네임");
        mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("MyPage");

        textemail.setText(emailget);

        btnsaveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(checkRegister);
            }
        });

        mDatabaseRef.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nicknameoriginal = userInfo1.getNickname();
                    editnickname.setText(nicknameoriginal);
                }catch(Exception e){
                    editnickname.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void uploadUserInfo(){
        UserInfo userInfo= new UsersInfo(emailget, editnickname.getText().toString().trim());
        String nicknameget= editnickname.getText().toString().trim();
        String emailget1 = user.getEmail();

        if(nicknameoriginal == null) {
            mDatabaseRef.child(id).child("닉네임").setValue(userInfo);
            mDatabase.child(nicknameget).setValue(userInfo);
        }

        if(nicknameoriginal != null) {
            userNicknameUpdates.put("nickname", editnickname.getText().toString().trim());
            usersRef.updateChildren(userNicknameUpdates);
            mDatabase.child(nicknameoriginal).removeValue();
            mDatabase.child(nicknameget).setValue(userInfo);
        }
    }

}
