package com.example.user.holoyolo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class OtherFollowerActivity extends AppCompatActivity implements OtherFollowerAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<FollowingData4> mFollows;
    private OtherFollowerAdapter fAdapter;

    private FirebaseDatabase database;
    private DatabaseReference followreference;

    private ChildEventListener DBListener;
    String nicknameoriginal;

    final String id = FirebaseInstanceId.getInstance().getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower);

        Intent intent =getIntent();
        final String id1 = intent.getExtras().getString("id1");

        database = FirebaseDatabase.getInstance();
        followreference = database.getReference("MyPage").child(id1).child("팔로워");

        mFollows = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.msgLv);

        fAdapter = new OtherFollowerAdapter(mFollows);
        fAdapter.setOnItemClickListener(OtherFollowerActivity.this);

        recyclerView.setAdapter(fAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layout);


        DBListener = followreference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FollowingData4 model = dataSnapshot.getValue(FollowingData4.class);
                mFollows.add(model);
                model.setkey(dataSnapshot.getKey());
                fAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mFollows.clear();

                FollowingData4 model = dataSnapshot.getValue(FollowingData4.class);
                model.setkey(dataSnapshot.getKey());
                int index = getItemIndex(model);

                mFollows.set(index, model);
                fAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                FollowingData4 model = dataSnapshot.getValue(FollowingData4.class);
                model.setkey(dataSnapshot.getKey());
                int index = getItemIndex(model);

                mFollows.remove(index);
                fAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private int getItemIndex(FollowingData4 user){

        int index = -1;

        for (int i=0 ; i<mFollows.size(); i++){
            if (mFollows.get(i).key.equals(user.key)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"Normal click at position:"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {

        String id1 = mFollows.get(position).getId();
        String nickname = mFollows.get(position).getNickname();
        if(id.equals(id1)){

            Intent intent = new Intent(OtherFollowerActivity.this, MypageActivity.class);
            intent.putExtra("nickname", nickname);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        else if (!id1.equals(id)){
            Intent intent = new Intent(OtherFollowerActivity.this, OthersPageActivity.class);
            intent.putExtra("nickname", nickname);
            intent.putExtra("id1", id1);
            startActivity(intent);
        }
    }

}
