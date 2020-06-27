package com.example.user.holoyolo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    final String id = FirebaseInstanceId.getInstance().getId();
    private int num;
    boolean heartfull;

    public ImageAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        final Upload uploadCurrent = mUploads.get(position);
        final String selectedKey =  uploadCurrent.getKey();
        holder.textViewName.setText(uploadCurrent.getName());
        final String category = uploadCurrent.getCategory();


        ValueEventListener checkStar = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                long starchildCount = dataSnapshot.getChildrenCount();
                holder.textstarCount.setText(String.valueOf(starchildCount));
                database.getInstance().getReference().child("Users").child(category).child("공개").child(selectedKey).child("starCount").setValue(starchildCount);

                while (child.hasNext()) {//마찬가지로 중복 유무 확인
                    if (id.equals(child.next().getKey())) { //이미하트눌러놓음
                        heartfull=true;
                        holder.starButton.setImageResource(R.drawable.heartfull);
                        return;
                    }else{
                        heartfull=false;
                        holder.starButton.setImageResource(R.drawable.heartless);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        if(category.equals("음식")){
            database.getInstance().getReference().child("Users").child("음식").child("공개").child(selectedKey).child("star").addListenerForSingleValueEvent(checkStar);
        }
        if(category.equals("여행")){
            database.getInstance().getReference().child("Users").child("여행").child("공개").child(selectedKey).child("star").addListenerForSingleValueEvent(checkStar);
        }
        if(category.equals("문화")){
            database.getInstance().getReference().child("Users").child("문화").child("공개").child(selectedKey).child("star").addListenerForSingleValueEvent(checkStar);
        }
        if(category.equals("버킷리스트")){
            database.getInstance().getReference().child("Users").child("버킷리스트").child("공개").child(selectedKey).child("star").addListenerForSingleValueEvent(checkStar);
        }

        holder.starButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(!heartfull){
                    holder.starButton.setImageResource(R.drawable.heartfull);
                    if(category.equals("음식")){
                        database.getInstance().getReference().child("Users").child("음식").child("공개").child(selectedKey).child("star").child(id).setValue(id);
                        heartfull=true;
                    }
                    if(category.equals("여행")){
                        database.getInstance().getReference().child("Users").child("여행").child("공개").child(selectedKey).child("star").child(id).setValue(id);
                        heartfull=true;
                    }
                    if(category.equals("문화")){
                        database.getInstance().getReference().child("Users").child("문화").child("공개").child(selectedKey).child("star").child(id).setValue(id);
                        heartfull=true;
                    }
                    if(category.equals("버킷리스트")) {
                        database.getInstance().getReference().child("Users").child("버킷리스트").child("공개").child(selectedKey).child("star").child(id).setValue(id);
                        heartfull=true;
                    }

                }
                else if(heartfull){
                    holder.starButton.setImageResource(R.drawable.heartless);
                    if(category.equals("음식")){
                        database.getInstance().getReference().child("Users").child("음식").child("공개").child(selectedKey).child("star").child(id).setValue(null);
                        heartfull=false;
                    }
                    if(category.equals("여행")){
                        database.getInstance().getReference().child("Users").child("여행").child("공개").child(selectedKey).child("star").child(id).setValue(null);
                        heartfull=false;
                    }
                    if(category.equals("문화")){
                        database.getInstance().getReference().child("Users").child("문화").child("공개").child(selectedKey).child("star").child(id).setValue(null);
                        heartfull=false;
                    }
                    if(category.equals("버킷리스트")){
                        database.getInstance().getReference().child("Users").child("버킷리스트").child("공개").child(selectedKey).child("star").child(id).setValue(null);
                        heartfull=false;
                    }

                }

            }
        });

        Picasso.with(mContext)
                .load(uploadCurrent.getFilePath())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerInside() //centerCrop
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView textViewName;
        public ImageView imageView;
        public ImageView starButton;
        public TextView textstarCount;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            starButton = itemView.findViewById(R.id.imageView2);
            textstarCount = itemView.findViewById(R.id.text_view_starcount);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){

                    switch (menuItem.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
