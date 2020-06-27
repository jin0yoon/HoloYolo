package com.example.user.holoyolo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class TabMyBucketAuthor extends Fragment implements BucketAuthorAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private BucketAuthorAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<OtherBucketItem> mUploads;

    final String id = FirebaseInstanceId.getInstance().getId();

    public TabMyBucketAuthor(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_mybucket, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        mRecyclerView.setLayoutManager(layoutManager);//레이아웃 관리자 설정

        mProgressCircle = rootView.findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new BucketAuthorAdapter(getContext(), mUploads);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(TabMyBucketAuthor.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyPage").child(id).child("버킷리스트").child("인증");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    OtherBucketItem upload = postSnapshot.getValue(OtherBucketItem.class);
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
        OtherBucketItem selectedItem = mUploads.get(position);
        String id1 = selectedItem.getUserId();
        String nickname= selectedItem.getNickname();
        String subject = selectedItem.getSubject();
        String contents=selectedItem.getContents();
        String seem = selectedItem.getSeem();
        String filepath=selectedItem.getFilePath();
        String category= selectedItem.getCategory();
        String key= selectedItem.getKey();

        Intent intent = new Intent(TabMyBucketAuthor.this.getActivity(), OtherBucketViewActivity.class);

        intent.putExtra("id1", id1);
        intent.putExtra("subject", subject);
        intent.putExtra("nickname", nickname);
        intent.putExtra("contents", contents);
        intent.putExtra("seem", seem);
        intent.putExtra("url", filepath);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position) {
    }

    @Override
    public void onDeleteClick(int position) {
        OtherBucketItem selectedItem = mUploads.get(position);
        String id1 = selectedItem.getUserId();
        final String selectedKey = selectedItem.getKey();
        final String id = FirebaseInstanceId.getInstance().getId();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getFilePath());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(TabMyBucketAuthor.this.getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



