package com.example.user.holoyolo;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.google.android.gms.location.places.Place;
import java.util.HashMap;
import java.util.Map;


public class BoardChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText boardchangesubjectview; //제목보여주는 곳
    private ImageView boardchangeimageview; //이미지 보여주는 곳
    private EditText boardchangecontentsview; //내용 보여주는 곳
    private TextView editTextLatitude2;
    private TextView editTextLongtitude2;
    private Button btnsave2;
    private int count;
    final String id = FirebaseInstanceId.getInstance().getId();
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase ;
    String info;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Map<String, Object> changeContent = new HashMap<>();
    Map<String, Object> changeSubject = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardchange);
        Intent intent1 = getIntent();

        boardchangesubjectview = (EditText) findViewById(R.id.boardchangesubjectview);
        boardchangeimageview = (ImageView) findViewById(R.id.boardchangeimageview); //게시글의 사진 보기
        boardchangecontentsview = (EditText) findViewById(R.id.boardchangecontentsview);
        editTextLatitude2=(TextView) findViewById(R.id.editTextLatitude2);
        editTextLongtitude2=(TextView)findViewById(R.id.editTextLongtitude2);
        btnsave2=(Button)findViewById(R.id.btnsave2);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("MyPage");
        mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Users");
        final String filePath = intent1.getExtras().getString("url");
        Picasso.with(this).load(filePath).into(boardchangeimageview);

        final String name = intent1.getExtras().getString("name");
        final String contents = intent1.getExtras().getString("contents");
        final String id1 = intent1.getExtras().getString("id1");
        final String category = intent1.getExtras().getString("category");
        final String key = intent1.getExtras().getString("key");
        final double latitude = intent1.getExtras().getDouble("latitude");
        final double longtitude = intent1.getExtras().getDouble("longtitude");

        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longtitude);

        boardchangesubjectview.setText(name);
        boardchangecontentsview.setText(contents);

        editTextLatitude2.setText(lat);
        editTextLongtitude2.setText(lon);

        editTextLatitude2.setFocusable(false);
        editTextLatitude2.setClickable(false);
        editTextLatitude2.setVisibility(View.GONE);

        editTextLongtitude2.setFocusable(false);
        editTextLongtitude2.setClickable(false);
        editTextLongtitude2.setVisibility(View.GONE);

        btnsave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjects = boardchangesubjectview.getText().toString().trim();
                String contents = boardchangecontentsview.getText().toString().trim();

                if (boardchangesubjectview.getText().toString().trim().length() != 0 &&
                        boardchangecontentsview.getText().toString().trim().length() != 0) {
                    changeContent.put("contents", boardchangecontentsview.getText().toString().trim());
                    changeSubject.put("name", subjects);

                    mDatabase.child(id).child(category).child(key).updateChildren(changeContent);
                    mDatabaseRef.child(category).child("공개").child(key).updateChildren(changeContent);
                    mDatabase.child(id).child(category).child(key).updateChildren(changeSubject);
                    mDatabaseRef.child(category).child("공개").child(key).updateChildren(changeSubject);

                    Intent intent = new Intent(BoardChangeActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(BoardChangeActivity.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(boardchangesubjectview.getText().toString().trim().length() == 0 ){
                    Toast.makeText(BoardChangeActivity.this, "제목을 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else if(boardchangecontentsview.getText().toString().trim().length() == 0){
                    Toast.makeText(BoardChangeActivity.this, "내용을 채워주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

