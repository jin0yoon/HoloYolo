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

import com.google.firebase.iid.FirebaseInstanceId;


public class  MybucketlistActivity extends AppCompatActivity {
        private TabLayout tabLayout;
        private AppBarLayout appBarLayout;
        private ViewPager viewPager;
        private Button btn_add;
        private TextView id;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Intent intent = getIntent();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mybucketlist);

            final String nickname = intent.getExtras().getString("nickname");
            id=(TextView)findViewById(R.id.Id);
            id.setText(nickname);

            btn_add=(Button)findViewById(R.id.btn_add);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
            viewPager = (ViewPager) findViewById(R.id.container);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.AddFragment(new TabMyBucket(), "MY");
            adapter.AddFragment(new TabMyBucketAuthor(), "인증");
            adapter.AddFragment(new TabMyBucketShare(), "공유");

            btn_add.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MybucketlistActivity.this, BucketListWriteActivity.class);
                    startActivity(intent);
                }
            });

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

        }
    }