package com.example.user.holoyolo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

public class BoardViewActivity extends AppCompatActivity implements ReplyAdapter.OnItemClickListener {
    private TextView nicknametextview; //닉네임 보여주는 곳
    private TextView idtextview; //아이디 보여주는 곳
    private TextView subjectview; //제목보여주는 곳
    private ImageView imageView; //이미지 보여주는 곳
    private TextView contentsview1; //내용 보여주는 곳
    private Button btnproceed;
    private ImageView heart; //좋아요 버튼
    private EditText replyedit;
    private Button replyenter;
    private Button btnchange;
    private Button sharebtn;
    private Button btndelete;
    private Button btndirection;

    private Upload upload;

    private String mImageUrl;

    private int count;
    String id = FirebaseInstanceId.getInstance().getId();

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;

    private DatabaseReference replyreference;
    private ValueEventListener rListener;

    private FirebaseAuth auth;
    String replyid;

    LinearLayoutManager mLinearLayoutManager;

    private RecyclerView recyclerView;
    private ReplyAdapter rAdapter;
    private List<ReplyModel> mReplys;

    private List<Upload> mUploads;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ChildEventListener DBListener;
    String info;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String nicknameoriginal;
    private List<Upload> uUploads = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();

    LocationListener locationListener;
    LocationManager locationManager;

    Double nowlat;
    Double nowlon;

    String ImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardview);
        final Intent intent = getIntent();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btndelete = (Button) findViewById(R.id.btndelete);
        btnchange = (Button) findViewById(R.id.btnchange);
        btnproceed = (Button) findViewById(R.id.btnproceed);
        nicknametextview = (TextView) findViewById(R.id.nicknametextview);
//        idtextview =(TextView) findViewById(R.id.idtextview);
        subjectview = (TextView) findViewById(R.id.subjectview);
        imageView = (ImageView) findViewById(R.id.imageview); //게시글의 사진 보기
        contentsview1 = (TextView) findViewById(R.id.contentsview1);
        replyedit = (EditText) findViewById(R.id.reply);
        replyenter = (Button) findViewById(R.id.replyenter);
        sharebtn = (Button) findViewById(R.id.sharebtn);
        btndirection = (Button) findViewById(R.id.btndirection);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage");
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        final String filePath = intent.getExtras().getString("url");
        Picasso.with(this).load(filePath).into(imageView);

        final int starCount = intent.getExtras().getInt("starCount");
        final String nickname = intent.getExtras().getString("nickname");
        final String name = intent.getExtras().getString("name");
        final String contents = intent.getExtras().getString("contents");
        final String seem = intent.getExtras().getString("seem");
        final String id1 = intent.getExtras().getString("id1");
        final String category = intent.getExtras().getString("category");
        final String key = intent.getExtras().getString("key");
        final double latitude = intent.getExtras().getDouble("latitude");
        final double longtitude = intent.getExtras().getDouble("longtitude");
        final String placename = intent.getExtras().getString("placename");

        replyreference = mDatabaseRef.child(category).child(seem).child(key).child("댓글");

        subjectview.setText(name);
        contentsview1.setText(contents);
        recyclerView = findViewById(R.id.comment_list);
        mReplys = new ArrayList<>();

        rAdapter = new ReplyAdapter(mReplys);

        rAdapter.setOnItemClickListener(BoardViewActivity.this);

        recyclerView.setAdapter(rAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);

        DBListener = replyreference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ReplyModel model = dataSnapshot.getValue(ReplyModel.class);
                mReplys.add(model);
                model.setkey(dataSnapshot.getKey());
                rAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mReplys.clear();

                ReplyModel model = dataSnapshot.getValue(ReplyModel.class);
                model.setkey(dataSnapshot.getKey());
                int index = getItemIndex(model);

                mReplys.set(index, model);
                rAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ReplyModel model = dataSnapshot.getValue(ReplyModel.class);
                model.setkey(dataSnapshot.getKey());
                int index = getItemIndex(model);

                mReplys.remove(index);
                rAdapter.notifyItemRemoved(index);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (!id1.equals(id)) {
            btnchange.setFocusable(false);
            btnchange.setClickable(false);
            btnchange.setVisibility(View.GONE);
        }

        if (!id1.equals(id)) {
            btndelete.setFocusable(false);
            btndelete.setClickable(false);
            btndelete.setVisibility(View.GONE);
        }
        mDatabase.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nicknameoriginal = userInfo1.getNickname();
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        nicknametextview.setText(nickname);

        btnchange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(BoardViewActivity.this, BoardChangeActivity.class);
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

        btnproceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BoardViewActivity.this, MapsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longtitude", longtitude);
                startActivity(intent);
            }
        });

        nicknametextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.equals(id1)){
                    Intent intent = new Intent(BoardViewActivity.this, MypageActivity.class);
                    intent.putExtra("nickname", nicknameoriginal);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                else if (!id1.equals(id)){
                    Intent intent = new Intent(BoardViewActivity.this, OthersPageActivity.class);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("id1", id1);
                    startActivity(intent);
                }
            }
        });

        replyenter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (replyedit.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                if (replyedit.getText().toString().length() != 0) {
                    count = ((int) (Math.random() * 1000)) + 1;
                    final String reply1 = replyedit.getText().toString();
                    ReplyModel reply = new ReplyModel(id, nicknameoriginal, reply1);
                    mDatabase.child(id1).child(category).child(key).child("댓글").push().setValue(reply);
                    mDatabase.child(id1).child("other").child(category).child(key).child("댓글").push().setValue(reply);
                    mDatabaseRef.child(category).child(seem).child(key).child("댓글").push().setValue(reply);
                    replyedit.getText().clear();
                }
            }
        });

        sharebtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Upload upload = new Upload(nickname, id1, category, name, seem, placename, latitude, longtitude, contents, filePath, starCount);
                mDatabase.child(id).child("공유").push().setValue(upload);
                Toast.makeText(getApplicationContext(), "공유되었습니다", Toast.LENGTH_SHORT).show();
            }
        });


        btndelete.setOnClickListener(new View.OnClickListener() { // ImageButton을 Click시 AlertDialog가 생성되도록 아래과 같이 설계
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardViewActivity.this); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                builder.setTitle("게시글을 삭제할까요?"); // AlertDialog.builder를 통해 Title text를 입력
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            StorageReference imageRef = mStorage.getReferenceFromUrl(filePath);
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mDatabaseRef.child(category).child(seem).child(key).removeValue();
                                    mDatabase.child(id).child(category).child(key).removeValue();
                                    mDatabase.child(id).child("other").child(category).child(key).removeValue();
                                    Toast.makeText(BoardViewActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        BoardViewActivity.this.finish(); // App.의 종료. Activity 생애 주기 참고
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Negative Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // "아니오" button이 받은 DialogInterface를 dismiss 하여 MainView로 돌아감
                    }
                });
                AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                dialog.show(); // dialog를 화면에 뿌려 줌
            }
        });


        btndirection.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = null;
                GpsPermissionCheckForMashMallo(); // android 6.0 이상에서 호출해주어야 합니다.
                if (locationManager != null) { // 위치정보 수집이 가능한 환경인지 검사.
                    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (isGPSEnabled || isNetworkEnabled) {
                        Log.e("GPS Enable", "true");

                        final List<String> m_lstProviders = locationManager.getProviders(false);
                        locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
                                double nowlatitude = location.getLatitude();
                                double nowlongtitude = location.getLongitude();
                                nowlat = nowlatitude;
                                nowlon = nowlongtitude;

                                if (ActivityCompat.checkSelfPermission(BoardViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(BoardViewActivity.this,
                                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                locationManager.removeUpdates(locationListener);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                Log.e("onStatusChanged", "onStatusChanged");
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                Log.e("onProviderEnabled", "onProviderEnabled");
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                Log.e("onProviderDisabled", "onProviderDisabled");
                            }
                        };

                        // QQQ: 시간, 거리를 0 으로 설정하면 가급적 자주 위치 정보가 갱신되지만 베터리 소모가 많을 수 있다.

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (String name : m_lstProviders) {
                                    if (ActivityCompat.checkSelfPermission(BoardViewActivity.this,
                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                            && ActivityCompat.checkSelfPermission(BoardViewActivity.this,
                                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    locationManager.requestLocationUpdates(name, 1000, 0, locationListener);
                                }

                            }
                        });

                    } else {
                        Log.e("GPS Enable", "false");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
                    }
                }

                uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=35.847507, 127.131122&daddr=" + latitude + "," + longtitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private int getItemIndex(ReplyModel user){

        int index = -1;

        for (int i=0 ; i<mReplys.size(); i++){
            if (mReplys.get(i).key.equals(user.key)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void GpsPermissionCheckForMashMallo() {

        //마시멜로우 버전 이하면 if문에 걸리지 않습니다.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS 사용 허가 요청");
            alertDialog.setMessage("앰버요청 발견을 알리기위해서는 사용자의 GPS 허가가 필요합니다.\n('허가'를 누르면 GPS 허가 요청창이 뜹니다.)");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("허가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BoardViewActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }
                    });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("거절",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onWhatEverClick(int position) {
    }

    @Override
    public void onDeleteClick(int position) {
        String id2 = mReplys.get(position).getId();
        if (!id2.equals(id)) {
            Toast.makeText(BoardViewActivity.this, "삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else if(id2.equals(id)) {
            replyreference.child(mReplys.get(position).key).removeValue();
            Toast.makeText(BoardViewActivity.this, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}