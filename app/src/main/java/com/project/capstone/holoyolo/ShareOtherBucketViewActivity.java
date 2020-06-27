package com.example.user.holoyolo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ShareOtherBucketViewActivity extends AppCompatActivity implements ReplyAdapter.OnItemClickListener {
    private TextView nicknametextview; //닉네임 보여주는 곳
    private TextView idtextview; //아이디 보여주는 곳
    private TextView subjectview; //제목보여주는 곳
    private ImageView imageView; //이미지 보여주는 곳
    private TextView contentsview1; //내용 보여주는 곳
    private EditText replyedit;
    private Button replyenter;
    private Button btnchange;
    private Button sharebtn;
    private Button btndelete;

    private OtherBucketItem upload;

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

    private List<OtherBucketItem> mUploads;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ChildEventListener DBListener;
    String info;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String nicknameoriginal;

    String ImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucketview);
        final Intent intent = getIntent();

        btndelete = (Button) findViewById(R.id.btndelete);
        btnchange = (Button) findViewById(R.id.btnchange);
        nicknametextview = (TextView) findViewById(R.id.nicknametextview);
        subjectview = (TextView) findViewById(R.id.subjectview);
        imageView = (ImageView) findViewById(R.id.imageview); //게시글의 사진 보기
        contentsview1 = (TextView) findViewById(R.id.contentsview1);
        replyedit = (EditText) findViewById(R.id.reply);
        replyenter = (Button) findViewById(R.id.replyenter);
        sharebtn = (Button) findViewById(R.id.sharebtn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage");
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        final String filePath = intent.getExtras().getString("url");
        Picasso.with(this).load(filePath).into(imageView);
        final String nickname = intent.getExtras().getString("nickname");
        final String subject = intent.getExtras().getString("subject");
        final String contents = intent.getExtras().getString("contents");
        final String seem = intent.getExtras().getString("seem");
        final String id1 = intent.getExtras().getString("id1");
        final String key = intent.getExtras().getString("key");

        replyreference = mDatabaseRef.child("버킷리스트").child(seem).child(key).child("댓글");

        subjectview.setText(subject);
        contentsview1.setText(contents);

        replyedit.setFocusable(false);
        replyedit.setClickable(false);
        replyedit.setVisibility(View.GONE);

        replyenter.setFocusable(false);
        replyenter.setClickable(false);
        replyenter.setVisibility(View.GONE);

        sharebtn.setFocusable(false);
        sharebtn.setClickable(false);
        sharebtn.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.comment_list);
        mReplys = new ArrayList<>();

        rAdapter = new ReplyAdapter(mReplys);

        rAdapter.setOnItemClickListener(ShareOtherBucketViewActivity.this);

        recyclerView.setAdapter(rAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);

        DBListener = replyreference.addChildEventListener(new ChildEventListener() {

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
                Intent intent1 = new Intent(ShareOtherBucketViewActivity.this, BucketBoardChangeActivity.class);
                intent1.putExtra("subject", subject);
                intent1.putExtra("contents", contents);
                intent1.putExtra("url", filePath);
                intent1.putExtra("key", key);
                intent1.putExtra("seem", seem);
                startActivity(intent1);
            }
        });


        nicknametextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareOtherBucketViewActivity.this, OthersPageActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("id1", id1);
                startActivity(intent);
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
                    mDatabase.child(id1).child("other").child("버킷리스트").child(key).child("댓글").push().setValue(reply);
                    mDatabase.child(id1).child("버킷리스트").child("인증").child(key).child("댓글").push().setValue(reply);
                    mDatabaseRef.child("버킷리스트").child("공개").child(key).child("댓글").push().setValue(reply);
                    replyedit.getText().clear();
                }
            }
        });

        sharebtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OtherBucketItem upload = new OtherBucketItem(id1, nickname, subject, contents, seem, filePath);
                mDatabase.child(id).child("버킷리스트공유").push().setValue(upload);
                Toast.makeText(getApplicationContext(), "공유되었습니다", Toast.LENGTH_SHORT).show();
            }
        });


        btndelete.setOnClickListener(new View.OnClickListener() { // ImageButton을 Click시 AlertDialog가 생성되도록 아래과 같이 설계
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShareOtherBucketViewActivity.this); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                builder.setTitle("게시글을 삭제할까요?"); // AlertDialog.builder를 통해 Title text를 입력
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            StorageReference imageRef = mStorage.getReferenceFromUrl(filePath);
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mDatabaseRef.child("버킷리스트").child(seem).child(key).removeValue();
                                    mDatabase.child(id).child("버킷리스트").child("인증").child(key).removeValue();
                                    mDatabase.child(id).child("other").child("버킷리스트").child(key).removeValue();
                                    Toast.makeText(ShareOtherBucketViewActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        ShareOtherBucketViewActivity.this.finish(); // App.의 종료. Activity 생애 주기 참고
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
            Toast.makeText(ShareOtherBucketViewActivity.this, "삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else if(id2.equals(id)) {
            replyreference.child(mReplys.get(position).key).removeValue();
            Toast.makeText(ShareOtherBucketViewActivity.this, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

}