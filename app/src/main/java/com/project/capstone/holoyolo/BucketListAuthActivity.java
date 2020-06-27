package com.example.user.holoyolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

public class BucketListAuthActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;

    final String id = FirebaseInstanceId.getInstance().getId();

    private Button authbtn;
    private TextView authbucketlisteditTextName;
    private TextView authbucketlisteditTextContents;

    private TextView nickname;

    String nickname1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucketlistauth);
        Intent intent= getIntent();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("MyPage");

        authbtn=(Button)findViewById(R.id.authbtn);
        authbucketlisteditTextName=(TextView)findViewById(R.id.authbucketlisteditTextName);
        authbucketlisteditTextContents=(TextView)findViewById(R.id.authbucketlisteditTextContents);

        final String  subject =intent.getExtras().getString("subject");
        final String  contents =intent.getExtras().getString("contents");
        final String key = intent.getExtras().getString("key");


        authbucketlisteditTextName.setText(subject);
        authbucketlisteditTextContents.setText(contents);

        authbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BucketListAuthActivity.this, BucketListAuthActivity2.class);
                intent.putExtra("subject", subject);
                intent.putExtra("contents", contents);
                intent.putExtra("key", key);
                startActivity(intent);

                BucketListAuthActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
