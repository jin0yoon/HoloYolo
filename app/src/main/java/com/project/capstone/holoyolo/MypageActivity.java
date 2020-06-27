package com.example.user.holoyolo;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Iterator;


public class MypageActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private TextView myid;
    private Button fbutton;
    private Button followerButton;
    private TextView count1;
    private TextView count2;
    private FirebaseDatabase firebaseDatabase;

    final String id = FirebaseInstanceId.getInstance().getId();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage").child(id).child("팔로잉");
    private DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("MyPage").child(id).child("팔로워");

    private DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage").child(id);

    public String nickname2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Intent intent = getIntent();

        myid=(TextView)findViewById(R.id.Id);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        viewPager = (ViewPager) findViewById(R.id.container);
        fbutton = (Button) findViewById(R.id.following);
        followerButton = (Button) findViewById(R.id.follower);

        count1 = (TextView) findViewById(R.id.count1);
        count2 = (TextView) findViewById(R.id.count2);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                long followingCount = dataSnapshot.getChildrenCount();
                count2.setText(String.valueOf(followingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child2 = dataSnapshot.getChildren().iterator();

                long followerCount = dataSnapshot.getChildrenCount();
                count1.setText(String.valueOf(followerCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, FollowingActivity4.class );
                startActivity(intent);
            }
        });

        followerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, FollowerActivity.class );
                startActivity(intent);
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        final String nickname =intent.getExtras().getString("nickname");
        myid.setText(nickname);
        nickname2 = nickname;

        adapter.AddFragment(new TabFood(), "음식");
        adapter.AddFragment(new TabTrip(), "여행");
        adapter.AddFragment(new TabCulture(), "문화");
        adapter.AddFragment(new TabShare(), "공유");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                Intent intent1;
                intent1 = new Intent(MypageActivity.this, MybucketlistActivity.class);
                intent1.putExtra("nickname", nickname2);
                startActivity(intent1);
        }
    }
}