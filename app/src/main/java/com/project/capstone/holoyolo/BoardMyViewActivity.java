package com.example.user.holoyolo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoardMyViewActivity extends AppCompatActivity {
    private TextView subjectview; //제목보여주는 곳
    private ImageView imageView; //이미지 보여주는 곳
    private TextView contentsview1; //내용 보여주는 곳
    private Button btnproceed;
    private Button btnchange;
    private ImageView heart; //좋아요 버튼
    private EditText replyedit;
    private Button replyenter;

    private Upload upload;
    String nicknameoriginal;
    private String mImageUrl;
    private String userInfo;
    public String filePath;
    private int count;
    final String id = FirebaseInstanceId.getInstance().getId();
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private ValueEventListener mDBListener;
    private FirebaseAuth auth;

    LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recyclerView;

    private List<Upload> mUploads;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    String info;
    String InfoValue;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private List<Upload> uUploads = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardmyview);
        Intent intent = getIntent();
        btnchange = (Button) findViewById(R.id.btnchange2);
        btnproceed = (Button) findViewById(R.id.btnproceed2);
        subjectview = (TextView) findViewById(R.id.subjectview2);
        imageView = (ImageView) findViewById(R.id.imageview2); //게시글의 사진 보기
        contentsview1 = (TextView) findViewById(R.id.contentsview2);
        replyedit = (EditText) findViewById(R.id.reply2);
        replyenter = (Button) findViewById(R.id.replyenter2);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage");
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        final String filePath = intent.getExtras().getString("url");
        Picasso.with(this).load(filePath).into(imageView);

        final String name = intent.getExtras().getString("name");
        final String contents = intent.getExtras().getString("contents");
        final String nickname = intent.getExtras().getString("nickname");
        final String id1 = intent.getExtras().getString("id1");
        final String category = intent.getExtras().getString("category");
        final String key = intent.getExtras().getString("key");

        final double latitude = intent.getExtras().getDouble("latitude");
        final double longtitude = intent.getExtras().getDouble("longtitude");

        subjectview.setText(name);
        contentsview1.setText(contents);

        mDatabase.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nicknameoriginal = userInfo1.getNickname();
                }catch(Exception e){
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        btnchange.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(BoardMyViewActivity.this, BoardChangeActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("contents", contents);
                intent1.putExtra("category", category);
                intent1.putExtra("url", filePath);
                intent1.putExtra("key", key);
                intent1.putExtra("latitude", latitude);
                intent1.putExtra("longtitude", longtitude);
                startActivity(intent1);
            }
        });

        btnproceed.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(BoardMyViewActivity.this, MapsActivity.class );
                intent.putExtra("name", name);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longtitude", longtitude);
                startActivity(intent);
            }
        });

        replyenter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                if(replyedit != null){
                    count = ((int)(Math.random()*1000))+1;
                    final String reply1= replyedit.getText().toString();
                    Reply reply=new Reply(id, nicknameoriginal, reply1);
                    mDatabase.child(id).child(category).child(key).child("댓글").child(id).child(id + count).setValue(reply);
                    mDatabaseRef.child(category).child("공개").child(key).child("댓글").push().setValue(reply);

                    replyedit.getText().clear();
                }
            }
        });
    }
}