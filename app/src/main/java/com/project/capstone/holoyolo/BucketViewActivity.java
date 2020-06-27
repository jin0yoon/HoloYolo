package com.example.user.holoyolo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BucketViewActivity extends AppCompatActivity {
    private TextView nicknametextview; //닉네임 보여주는 곳
    private TextView idtextview; //아이디 보여주는 곳
    private TextView subjectview; //제목보여주는 곳
    private ImageView imageView; //이미지 보여주는 곳
    private TextView contentsview1; //내용 보여주는 곳
    private Button sharebtn; //공유하는 버튼

    private Upload upload;

    private String mImageUrl;

    private int count;
    String id = FirebaseInstanceId.getInstance().getId();
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseAuth auth;


    LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recyclerView;

    private List<Upload> mUploads;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    String info;
    String InfoValue;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String nicknameoriginal;
    private List<Upload> uUploads = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();

    String ImgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucketview);
        Intent intent = getIntent();

        nicknametextview=(TextView)findViewById(R.id.nicknametextview);
        subjectview=(TextView) findViewById(R.id.subjectview);
        imageView=(ImageView) findViewById(R.id.imageview); //게시글의 사진 보기
        contentsview1=(TextView) findViewById(R.id.contentsview1);
        sharebtn=(Button)findViewById(R.id.sharebtn);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("MyPage");
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        auth=FirebaseAuth.getInstance();

        final String filePath =intent.getExtras().getString("url");
        Picasso.with(this).load(filePath).into(imageView);

        final String nickname =intent.getExtras().getString("nickname");
        final String name =intent.getExtras().getString("name");
        final String contents=intent.getExtras().getString("contents");
        final String seem=intent.getExtras().getString("seem");
        final String id1 = intent.getExtras().getString("id1");
        final String category=intent.getExtras().getString("category");
        final String key= intent.getExtras().getString("key");

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

        nicknametextview.setText(nickname);


        nicknametextview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(BucketViewActivity.this, OthersPageActivity.class );
                intent.putExtra("nickname", nickname);
                intent.putExtra("id1", id1);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}