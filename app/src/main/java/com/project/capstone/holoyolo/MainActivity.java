package com.example.user.holoyolo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar1;
    private SearchView searchView1;
    private TextInputLayout textInputLayout1;
    private TextInputEditText textInputEditText1;

    private BackPressCloseHandler backPressCloseHandler;

    private Button logout1;
    private Button imagebtn1;
    private Button imagebtn2;
    private Button imagebtn3;
    private Button imagebtn4;
    private Button imagebtn5;
    private Button imagebtn6;

    private Button menuImageButton;

    private TextView txtName, txtEmail;
    private FirebaseAuth mAuth;
    final String id = FirebaseInstanceId.getInstance().getId();
    private DatabaseReference mDatabaseRef;
    String nicknameoriginal;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-2867590642618355~6807187409");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE231").build();
        mAdView.loadAd(adRequest);

        imagebtn1 = (Button) findViewById(R.id.imageButton1);
        imagebtn2 = (Button) findViewById(R.id.imageButton2);
        imagebtn3 = (Button) findViewById(R.id.imageButton3);
        imagebtn4 = (Button) findViewById(R.id.imageButton4);
        imagebtn5 = (Button) findViewById(R.id.imageButton5);
        imagebtn6 = (Button) findViewById(R.id.imageButton6);
        logout1=(Button)findViewById(R.id.logout1);

        backPressCloseHandler = new BackPressCloseHandler(this);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("MyPage");

        menuImageButton = (Button) findViewById(R.id.btnMenu);
        menuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        mDatabaseRef.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
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

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
//                Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
            }
        });


        imagebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        imagebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                startActivity(intent);
            }
        });

        imagebtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CultureActivity.class);
                startActivity(intent);
            }
        });
        imagebtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BucketListActivity.class);
                startActivity(intent);
            }
        });
        imagebtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                intent.putExtra("nickname", nicknameoriginal);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        imagebtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardWritingActivity1.class);
                startActivity(intent);
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
            String Name = currentUser.getDisplayName();
            txtEmail.setText(currentUser.getEmail());
        }
        passPushTokenToServer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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

    void passPushTokenToServer(){
        String uId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);
        FirebaseDatabase.getInstance().getReference().child("MyPage").child(id).updateChildren(map);
    }

//    @Override public void onBackPressed() {
//        //super.onBackPressed();
//         backPressCloseHandler.onBackPressed();
//    }
}