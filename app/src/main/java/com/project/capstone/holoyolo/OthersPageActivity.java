package com.example.user.holoyolo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Iterator;

public class OthersPageActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private TextView Id1;
    private Button fplusbutton;
    private Button fbutton;
    private Button followerButton;
    private Button followingButton;

    private TextView count1;
    private TextView count2;

    public static String id123;
    final String id = FirebaseInstanceId.getInstance().getId();

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    private DatabaseReference mDatabase;
    private DatabaseReference myDatabase;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabaseRef;
    private String nickname1;

    private DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherspage);
        Intent intent =getIntent();

        Id1=(TextView)findViewById(R.id.Id);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        viewPager = (ViewPager) findViewById(R.id.container);
        fplusbutton = (Button) findViewById(R.id.button1);
        fbutton = (Button) findViewById(R.id.following);
        followerButton = (Button) findViewById(R.id.follower);
        followingButton = (Button) findViewById(R.id.following);

        final String id1 = intent.getExtras().getString("id1");
        final String nickname =intent.getExtras().getString("nickname");

        Id1.setText(nickname);
        id123=id1;

        count1 = (TextView) findViewById(R.id.count1);
        count2 = (TextView) findViewById(R.id.count2);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage").child(id1).child("팔로잉");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("MyPage").child(id1).child("팔로워");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("MyPage");

        mDatabaseRef.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nickname1 = userInfo1.getNickname();
                }catch(Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                long followingCount = dataSnapshot.getChildrenCount();
                count2.setText(String.valueOf(followingCount));

                //fDatabase.child("MyPage").child(id).child("팔로잉count").setValue(followingCount);
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

                //fDatabase.child("MyPage").child(id1).child("팔로워count").setValue(followerCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OthersPageActivity.this, OtherFollowingActivity.class );
                intent.putExtra("id1", id1);
                startActivity(intent);
            }
        });

        followerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OthersPageActivity.this, OtherFollowerActivity.class );
                intent.putExtra("id1", id1);
                startActivity(intent);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OthersPageActivity.this, OtherFollowingActivity.class );
                intent.putExtra("id1", id1);
                startActivity(intent);
            }
        });


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new OtherTabFood(), "음식");
        adapter.AddFragment(new OtherTabTrip(), "여행");
        adapter.AddFragment(new OtherTabCulture(), "문화");
        adapter.AddFragment(new OtherTabBucket(), "버킷");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference2 = FirebaseDatabase.getInstance().getReference();
                FollowingData4 followingData1=new FollowingData4(id1, nickname);
                FollowingData4 followingData2 =new FollowingData4(id, nickname1);
                databaseReference.child("MyPage").child(id).child("팔로잉").child(id1).setValue(followingData1);
                databaseReference2.child("MyPage").child(id1).child("팔로워").child(id).setValue(followingData2);


                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                        long followingCount = dataSnapshot.getChildrenCount();
                        count2.setText(String.valueOf(followingCount));

                        //fDatabase.child("MyPage").child(id).child("팔로잉count").setValue(followingCount);
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

                        //fDatabase.child("MyPage").child(id1).child("팔로워count").setValue(followerCount);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        });


    }

}