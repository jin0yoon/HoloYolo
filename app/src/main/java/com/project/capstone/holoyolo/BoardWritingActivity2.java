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
import android.widget.EditText;
import android.widget.ImageView;
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

public class BoardWritingActivity2 extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    private int count;

    private Button btnsave;
    private Button btngetplace1;
    private Button btnfileselect;

    private EditText editTextlatitude;
    private EditText editTextlongtitude;
    private EditText placename;

    private Uri mImageUri;

    String category;
    String name;
    String contents;
    String switchcheck;
    String nickname;
    String seem;

    private ImageView mImageView;
    View v;

    final String id = FirebaseInstanceId.getInstance().getId();
    private static final int PLACE_PICKER_REQUEST=1;
    private static final int PICK_IMAGE_REQUEST = 2;
    SimpleDateFormat timing = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.KOREA);
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardwrite2);
        Intent intent = getIntent();

        category = intent.getExtras().getString("category");
        name =intent.getExtras().getString("name");
        contents = intent.getExtras().getString("contents");
        switchcheck = intent.getExtras().getString("switchcheck");
        seem = intent.getExtras().getString("seem");
        nickname=intent.getExtras().getString("nickname");

        btnsave=(Button)findViewById(R.id.btnsave);
        btnfileselect=(Button)findViewById(R.id.button_choose_image);
        btngetplace1=(Button)findViewById(R.id.btngetgps1);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyPage");

        mImageView=(ImageView)findViewById(R.id.image_view);

        editTextlatitude=(EditText)findViewById(R.id.editTextLatitude);
        editTextlongtitude=(EditText)findViewById(R.id.editTextLongtitude);
        placename=(EditText) findViewById(R.id.placename);

        editTextlatitude.setFocusable(false);
        editTextlatitude.setClickable(false);
        editTextlatitude.setVisibility(View.GONE);

        editTextlongtitude.setFocusable(false);
        editTextlongtitude.setClickable(false);
        editTextlongtitude.setVisibility(View.GONE);

        //파일 고르는 곳
        btnfileselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //게시글 저장하는 곳
        btnsave.setOnClickListener(this);

        //지도에서 선택하는 곳
        btngetplace1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent= builder.build(BoardWritingActivity2.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                }catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
            }
        });
    }


    public void googlePlacePicker(View view){
        //place picker 함수를 부르는 부분
        PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();

        try{
            startActivityForResult(builder.build(BoardWritingActivity2.this), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch(GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            String latitude = String.valueOf(place.getLatLng().latitude);
            String longitude = String.valueOf(place.getLatLng().longitude);
            String placename1 = String.valueOf(place.getName().toString());

            editTextlatitude.setText(latitude);
            editTextlongtitude.setText(longitude);
            placename.setText(placename1);
        }
        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK&& data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private void uploadFile(){

        if(editTextlatitude.getText().toString().trim().length() ==0){
            Toast.makeText(BoardWritingActivity2.this, "장소를 선택해주세요", Toast.LENGTH_LONG).show();
        }
        if(editTextlongtitude.getText().toString().trim().length() ==0){
            Toast.makeText(BoardWritingActivity2.this, "장소를 선택해주세요", Toast.LENGTH_LONG).show();
        }
        if(mImageUri==null){
            Toast.makeText(BoardWritingActivity2.this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
        }

        if(mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String filePath=taskSnapshot.getDownloadUrl().toString();
                            int starCount = 0;
                            count = ((int)(Math.random()*1000))+1;
                            Toast.makeText(BoardWritingActivity2.this, "업로드를 성공하셨습니다", Toast.LENGTH_LONG).show();
                            Upload upload=new Upload(nickname, id, category, name, seem,
                                    placename.getText().toString().trim(),
                                    Double.parseDouble(editTextlatitude.getText().toString().trim()),
                                    Double.parseDouble(editTextlongtitude.getText().toString().trim()),
                                    contents, filePath, starCount);
                            String currentDate = timing.format(date);

                            if(switchcheck.equals("1")) {
                                if (category.equals("음식")) {
                                    mDatabase.child("음식").child("공개").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("음식").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("other").child("음식").child(currentDate+id).setValue(upload);
                                }
                                if (category.equals("여행")) {
                                    mDatabase.child("여행").child("공개").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("여행").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("other").child("여행").child(currentDate+id).setValue(upload);
                                }
                                if (category.equals("문화")) {
                                    mDatabase.child("문화").child("공개").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("문화").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("other").child("문화").child(currentDate+id).setValue(upload);
                                }
                            }
                            if(switchcheck.equals("0")) {
                                if (category.equals("음식")) {
                                    mDatabase.child("음식").child("비공개").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("음식").child(currentDate+id).setValue(upload);
                                }
                                if (category.equals("여행")) {
                                    mDatabase.child("여행").child("비공개").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("여행").child(currentDate+id).setValue(upload);
                                }
                                if (category.equals("문화")) {
                                    mDatabase.child("문화").child("비공개").child(currentDate+id).setValue(upload);
                                    mDatabaseRef.child(id).child("문화").child(currentDate+id).setValue(upload);
                                }
                            }

                            BoardWritingActivity2.this.finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BoardWritingActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }else {
            Toast.makeText(this,"파일을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
