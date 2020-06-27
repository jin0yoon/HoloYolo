package com.example.user.holoyolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BucketListWriteActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabaseRef;
    final String id = FirebaseInstanceId.getInstance().getId();

    private Button bucketlistsave;
    private TextView bucketlistnickname;
    private EditText bucketlisteditTextname;
    private EditText bucketlisteditTextcontents;

    String nickname1;

    View v;

    SimpleDateFormat timing = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.KOREA);
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucketlistwriting);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("MyPage");

        bucketlistsave = (Button) findViewById(R.id.bucketlistsave);
        bucketlisteditTextname = (EditText) findViewById(R.id.bucketlisteditTextName);
        bucketlisteditTextcontents = (EditText) findViewById(R.id.bucketlisteditTextContents);
        bucketlistnickname = (TextView) findViewById(R.id.bucketlistnickname);

        bucketlistnickname.setFocusable(false);
        bucketlistnickname.setClickable(false);
        bucketlistnickname.setVisibility(View.GONE);

        mDatabaseRef.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nickname1 = userInfo1.getNickname();
                    bucketlistnickname.setText(nickname1);
                }catch(Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        bucketlistsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bucketlisteditTextname.getText().toString().trim().length() == 0) {
                    Toast.makeText(BucketListWriteActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (bucketlisteditTextcontents.getText().toString().trim().length() == 0) {
                    Toast.makeText(BucketListWriteActivity.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                if (bucketlisteditTextname.getText().toString().trim().length() != 0 &&
                        bucketlisteditTextcontents.getText().toString().trim().length() != 0) {

                    Upload bucketListInfo = new Upload(id, bucketlistnickname.getText().toString().trim(),
                            bucketlisteditTextname.getText().toString().trim(),
                            bucketlisteditTextcontents.getText().toString().trim());

                    String currentDate = timing.format(date);

                    mDatabaseRef.child(id).child("버킷리스트").child("미인증").child(currentDate + id).setValue(bucketListInfo);
                    Toast.makeText(BucketListWriteActivity.this, "성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show();
                    BucketListWriteActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}