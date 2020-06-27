package com.example.user.holoyolo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class BucketListActivity extends AppCompatActivity implements OtherBucketAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private OtherBucketAdapter mAdapter;
    private ImageButton btnplus;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private ValueEventListener mDBListener;

    private List<OtherBucketItem> mAllUploads = new ArrayList<>();
    private List<OtherBucketItem> mDispUploads = new ArrayList<>();
    //검색어 필터
    private EditText editFilter;

    private BackPressCloseHandler backPressCloseHandler;

    private Button menuImageButton;
    private TextView txtName, txtEmail;
    private FirebaseAuth mAuth;
    String nicknameoriginal;

    final String id = FirebaseInstanceId.getInstance().getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        btnplus=(ImageButton)findViewById(R.id.boardplusfood);

        TextView textView = (TextView) findViewById(R.id.title) ;
        textView.setText("버킷리스트");

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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//        layoutManager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(layoutManager);//레이아웃 관리자 설정

        mProgressCircle = findViewById(R.id.progress_circle);

        mAllUploads = new ArrayList<>();

        mAdapter = new OtherBucketAdapter(BucketListActivity.this, mAllUploads);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child("버킷리스트").child("공개");
        mDatabase = FirebaseDatabase.getInstance().getReference("MyPage");
        mDatabase.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nicknameoriginal = userInfo1.getNickname();
                    txtName.setText(nicknameoriginal);
                }catch(Exception e){
                    txtName.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAllUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    OtherBucketItem upload = postSnapshot.getValue(OtherBucketItem.class);
                    upload.setkey(postSnapshot.getKey());
                    mAllUploads.add(upload);
                }
                mDispUploads.addAll(mAllUploads);
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BucketListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BucketListActivity.this, BucketListWriteActivity.class);
                startActivity(intent1);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance(); // 인스턴스 생성
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { // 만약 로그인이 되어있으면 다음 액티비티 실행

            View nav_header_view = navigationView.getHeaderView(0);
            txtName = (TextView) nav_header_view.findViewById(R.id.tvName);
            txtEmail = (TextView) nav_header_view.findViewById(R.id.tvEmail);
            txtEmail.setText(currentUser.getEmail());
        }
    }
    public void searchListItem(String searchText) {
        mDispUploads.clear();
        for(int i = 0;i<mAllUploads.size();i++){
        }
        if(searchText.length() == 0) {
            mDispUploads.addAll(mAllUploads);
        }
        else {
            for( OtherBucketItem item : mAllUploads) {
                if(item.getName().contains(searchText)) {
                    mDispUploads.add(item);
                }
            }
        }

        mAdapter = new OtherBucketAdapter(this, mDispUploads);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        OtherBucketItem selectedItem = mDispUploads.get(position);
        String id1 = selectedItem.getUserId();
        String nickname= selectedItem.getNickname();
        String subject = selectedItem.getSubject();
        String contents=selectedItem.getContents();
        String seem = selectedItem.getSeem();
        String filepath=selectedItem.getFilePath();
        String key= selectedItem.getKey();

        mDatabase.child(id1).child("닉네임").addValueEventListener(new ValueEventListener() {
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

        Intent intent = new Intent(BucketListActivity.this, OtherBucketViewActivity.class);

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
        OtherBucketItem selectedItem = mDispUploads.get(position);
        String id1 = selectedItem.getUserId();
        final String selectedKey = selectedItem.getKey();
        final String id = FirebaseInstanceId.getInstance().getId();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getFilePath());
        if(id.equals(id1)) {
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(BucketListActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_food) {
            Intent intent = new Intent(this, FoodActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_culture) {
            Intent intent = new Intent(this, CultureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_trip) {
            Intent intent = new Intent(this, TripActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bucket) {
            Intent intent = new Intent(this, BucketListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mypage) {
            Intent intent = new Intent(this, MypageActivity.class);
            intent.putExtra("nickname", nicknameoriginal);
            intent.putExtra("id", id);
            startActivity(intent);
        } else if (id == R.id.nav_write) {
            Intent intent = new Intent(this, BoardWritingActivity1.class);
            startActivity(intent);
        } else if(id ==R.id.nav_changeinfo){
            Intent intent = new Intent(this, UserInfoChangeActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}