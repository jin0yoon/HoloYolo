package com.example.user.holoyolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class BoardWritingActivity1 extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabaseRef;
    final String id = FirebaseInstanceId.getInstance().getId();

    String menu;

    private Button popupbtn;
    private Button btnnext;

    private Switch aSwitch;

    private TextView seemcheck;
    private TextView seeunsee;
    private TextView switchcheck;
    private TextView editmenu;
    private TextView nickname;

    private EditText editTextname;
    private EditText editTextcontents;

    String nickname1;
    String seem;

    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardwrite1);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("MyPage");

        popupbtn=(Button)findViewById(R.id.popupbtn1);
        btnnext=(Button)findViewById(R.id.btnnext);

        aSwitch=(Switch)findViewById(R.id.switch1);
        aSwitch.setChecked(true);

        editTextname=(EditText)findViewById(R.id.editTextName);
        editTextcontents=(EditText)findViewById(R.id.editTextContents);
        editmenu=(TextView) findViewById(R.id.editmemu);
        switchcheck=(TextView)findViewById(R.id.switchcheck);
        nickname=(TextView)findViewById(R.id.nickname);
        seemcheck=(TextView)findViewById(R.id.seemcheck);
        seeunsee=(TextView)findViewById(R.id.seeunsee);

        editmenu.setFocusable(false);
        editmenu.setClickable(false);
        editmenu.setVisibility(View.GONE);

        switchcheck.setText("1");
        switchcheck.setFocusable(false);
        switchcheck.setClickable(false);
        switchcheck.setVisibility(View.GONE);

        nickname.setFocusable(false);
        nickname.setClickable(false);
        nickname.setVisibility(View.GONE);

        seemcheck.setText("공개");
        seemcheck.setFocusable(false);
        seemcheck.setClickable(false);
        seemcheck.setVisibility(View.GONE);

        mDatabaseRef.child(id).child("닉네임").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo1 userInfo1 = dataSnapshot.getValue(UserInfo1.class);
                try {
                    nickname1 = userInfo1.getNickname();
                    nickname.setText(nickname1);
                }catch(Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //카테고리 고르는 곳
        popupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(BoardWritingActivity1.this, popupbtn);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(BoardWritingActivity1.this,"카테고리는 : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        popupbtn.setText(item.getTitle());
                        editmenu.setText(popupbtn.getText().toString().trim());
                        return true;
                    }
                });
                popup.show();
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

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editmenu.getText().toString().trim().length() == 0 ){
                    Toast.makeText(BoardWritingActivity1.this, "카테고리를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(editTextname.getText().toString().trim().length() == 0 ){
                    Toast.makeText(BoardWritingActivity1.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(editTextcontents.getText().toString().trim().length() == 0 ){
                    Toast.makeText(BoardWritingActivity1.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                if (editmenu.getText().toString().trim().length() != 0 &&
                        editTextname.getText().toString().trim().length() != 0 &&
                        editTextcontents.getText().toString().trim().length() != 0 ) {
                    Intent intent = new Intent(BoardWritingActivity1.this, BoardWritingActivity2.class);
                    intent.putExtra("category", popupbtn.getText().toString().trim());
                    intent.putExtra("name", editTextname.getText().toString().trim());
                    intent.putExtra("contents", editTextcontents.getText().toString().trim());
                    intent.putExtra("seem", seemcheck.getText().toString().trim());
                    intent.putExtra("switchcheck", switchcheck.getText().toString().trim());
                    intent.putExtra("nickname", nickname.getText().toString().trim());
                    startActivity(intent);
                    BoardWritingActivity1.this.finish();
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

