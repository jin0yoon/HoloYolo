package com.example.user.holoyolo;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
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

public class MyPageBoardViewActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ImageButton btnplus;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Upload> mAllUploads = new ArrayList<>();
    private List<Upload> mDispUploads = new ArrayList<>();
    //검색어 필터
    private EditText editFilter;
    final String id = FirebaseInstanceId.getInstance().getId();
    private BackPressCloseHandler backPressCloseHandler;
    private FirebaseUser auth;

    private Button menuImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        btnplus=(ImageButton)findViewById(R.id.boardplusfood);

        editFilter = findViewById(R.id.edt_filter);
        editFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String searchText = editFilter.getText().toString();

                if( mAdapter != null )
                    searchListItem(searchText);
            }
        });

        menuImageButton = (Button) findViewById(R.id.btnMenu);
        menuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        mRecyclerView.setLayoutManager(layoutManager);//레이아웃 관리자 설정

        mProgressCircle = findViewById(R.id.progress_circle);

        mAllUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(MyPageBoardViewActivity.this, mAllUploads);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(MyPageBoardViewActivity.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyPage").child(id).child("음식");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAllUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setkey(postSnapshot.getKey());
                    mAllUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyPageBoardViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyPageBoardViewActivity.this, BoardWritingActivity1.class);
                startActivity(intent1);
            }
        });
    }

    public void searchListItem(String searchText) {

        mDispUploads.clear();

        for(int i = 0;i<mAllUploads.size();i++){
        }
        if(searchText.length() == 0)
        {
            mDispUploads.addAll(mAllUploads);
        }
        else
        {
            for( Upload item : mAllUploads)
            {
                if(item.getName().contains(searchText))
                {
                    mDispUploads.add(item);
                }
            }
        }

        mAdapter = new ImageAdapter(this, mDispUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Upload selectedItem = mAllUploads.get(position);
        String name = selectedItem.getName();
        String contents=selectedItem.getContents();
        String filepath=selectedItem.getFilePath();
        String category= selectedItem.getCategory();
        String key= selectedItem.getKey();
        double latitude=selectedItem.getLatitude();
        double longtitude=selectedItem.getLongtitude();

        Intent intent = new Intent(MyPageBoardViewActivity.this, BoardMyViewActivity.class);
        Toast.makeText(MyPageBoardViewActivity.this, "눌렀어 왜안돼", Toast.LENGTH_SHORT).show();
//        intent.putExtra("id1", id1);
        intent.putExtra("name", name);
        intent.putExtra("contents", contents);
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
        Upload selectedItem = mAllUploads.get(position);
        String id1 = selectedItem.getUserId();
        final String selectedKey = selectedItem.getKey();
        final String id = FirebaseInstanceId.getInstance().getId();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getFilePath());
        if(id.equals(id1)) {
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(MyPageBoardViewActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}