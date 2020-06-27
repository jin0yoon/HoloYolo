package com.example.user.holoyolo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BucketListAuthActivity2 extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private Switch aSwitch;

    private Button authbtnsave;
    private Button authbtnimage;

    private TextView authnickname;
    private EditText authbucketlist2editTextName;
    private EditText authbucketlist2editTextContents;
    private TextView seemcheck;
    private TextView seeunsee;
    private TextView switchcheck;

    private Uri mImageUri;

    String nickname1;

    private ImageView authimageview;
    View v;

    final String id = FirebaseInstanceId.getInstance().getId();
    private static final int PICK_IMAGE_REQUEST = 1;
    SimpleDateFormat timing = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.KOREA);
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucketlistauth2);
        Intent intent = getIntent();

        final String  subject =intent.getExtras().getString("subject");
        final String  contents =intent.getExtras().getString("contents");
        final String key= intent.getExtras().getString("key");

        aSwitch=(Switch)findViewById(R.id.switch1);
        aSwitch.setChecked(true);

        authbtnsave=(Button)findViewById(R.id.authbtnsave);
        authbtnimage=(Button)findViewById(R.id.authbtnimage);
        authnickname=(TextView)findViewById(R.id.authnickname);
        authbucketlist2editTextName=(EditText)findViewById(R.id.authbucketlist2editTextName);
        authbucketlist2editTextContents=(EditText)findViewById(R.id.authbucketlist2editTextContents);
        seemcheck=(TextView)findViewById(R.id.seemcheck);
        switchcheck=(TextView)findViewById(R.id.switchcheck);
        seeunsee=(TextView)findViewById(R.id.seeunsee);
        authimageview=(ImageView)findViewById(R.id.authimageview);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("bucketlist");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyPage");

        mDatabaseRef.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nickname1 = userInfo1.getNickname();
                    authnickname.setText(nickname1);
                } catch (Exception e) {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        authbucketlist2editTextName.setText(subject);
        authbucketlist2editTextContents.setText(contents);

        switchcheck.setText("1");
        switchcheck.setFocusable(false);
        switchcheck.setClickable(false);
        switchcheck.setVisibility(View.GONE);

        authnickname.setFocusable(false);
        authnickname.setClickable(false);
        authnickname.setVisibility(View.GONE);

        seemcheck.setText("공개");
        seemcheck.setFocusable(false);
        seemcheck.setClickable(false);
        seemcheck.setVisibility(View.GONE);

        //파일 고르는 곳
        authbtnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //게시글 저장하는 곳
        authbtnsave.setOnClickListener(this);
        authbtnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageUri==null){
                    Toast.makeText(BucketListAuthActivity2.this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
                }

                if(mImageUri != null) {
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));

                    mUploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String filePath=taskSnapshot.getDownloadUrl().toString();

                                    OtherBucketItem OtherBucketItem=new OtherBucketItem(id, authnickname.getText().toString().trim(),
                                            authbucketlist2editTextName.getText().toString().trim(),
                                            authbucketlist2editTextContents.getText().toString().trim(),
                                            seemcheck.getText().toString().trim(), filePath);
                                    String currentDate = timing.format(date);


                                    if(seemcheck.getText().toString().trim().equals("공개")){
                                        mDatabase.child("버킷리스트").child("공개").child(currentDate+id).setValue(OtherBucketItem);
                                        mDatabaseRef.child(id).child("other").child("버킷리스트").child(currentDate+id).setValue(OtherBucketItem);
                                        mDatabaseRef.child(id).child("버킷리스트").child("인증").child(currentDate+id).setValue(OtherBucketItem);
                                    }
                                    if(seemcheck.getText().toString().trim().equals("비공개")){
                                        mDatabase.child("버킷리스트").child("비공개").child(currentDate+id).setValue(OtherBucketItem);
                                        mDatabaseRef.child(id).child("버킷리스트").child("인증").child(currentDate+id).setValue(OtherBucketItem);
                                    }

                                    Toast.makeText(BucketListAuthActivity2.this, "업로드를 성공하셨습니다", Toast.LENGTH_LONG).show();
                                    BucketListAuthActivity2.this.finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BucketListAuthActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(BucketListAuthActivity2.this,"파일을 선택해주세요", Toast.LENGTH_SHORT).show();
                }




                mDatabaseRef.child(id).child("버킷리스트").child("미인증").child(key).removeValue();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    switchcheck.setText("1");
                    seemcheck.setText("공개");
                    seeunsee.setText("공개");

                }
                else if (isChecked == false) {
                    switchcheck.setText("0");
                    seemcheck.setText("비공개");
                    seeunsee.setText("비공개");
                }
            }
        });

    }

    private void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult (int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK ){
            mImageUri=data.getData();
            Picasso.with(this).load(mImageUri).into(authimageview);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}