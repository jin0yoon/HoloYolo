package com.example.user.holoyolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class OtherTabShare extends Fragment implements TabAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private TabAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Upload> mUploads;

    final String id = FirebaseInstanceId.getInstance().getId();

    String id1 = OthersPageActivity.id123;

    public OtherTabShare(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_share, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(),3);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        mRecyclerView.setLayoutManager(layoutManager);//레이아웃 관리자 설정

        mProgressCircle = rootView.findViewById(R.id.progress_circle);


        mUploads = new ArrayList<>();

        mAdapter = new TabAdapter(getContext(), mUploads);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(OtherTabShare.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyPage").child(id1).child("공유");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setkey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


        return rootView;
    }
    @Override
    public void onItemClick(int position) {
        Upload selectedItem = mUploads.get(position);
        String id1 = selectedItem.getUserId();
        String nickname= selectedItem.getNickname();
        String name = selectedItem.getName();
        String contents=selectedItem.getContents();
        String seem = selectedItem.getSeem();
        String filepath=selectedItem.getFilePath();
        String category= selectedItem.getCategory();
        String key= selectedItem.getKey();
        int starCount=selectedItem.getStarCount();
        double latitude=selectedItem.getLatitude();
        double longtitude=selectedItem.getLongtitude();

        Intent intent = new Intent(OtherTabShare.this.getActivity(), BoardViewActivity.class);

        intent.putExtra("starCount", starCount);
        intent.putExtra("id1", id1);
        intent.putExtra("name", name);
        intent.putExtra("nickname", nickname);
        intent.putExtra("contents", contents);
        intent.putExtra("seem", seem);
        intent.putExtra("url", filepath);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longtitude", longtitude);
        intent.putExtra("category", category);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position) {
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getFilePath());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
            }
        });

    }

}



