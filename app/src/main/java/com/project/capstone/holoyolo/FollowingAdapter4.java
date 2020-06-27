package com.example.user.holoyolo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;


public class FollowingAdapter4 extends RecyclerView.Adapter<FollowingAdapter4.FollowingViewHolder>{

    private List<FollowingData4> mFollows;
    private OnItemClickListener fListener;

    final String id = FirebaseInstanceId.getInstance().getId();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("MyPage");
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("MyPage").child(id).child("팔로잉");

    public FollowingAdapter4(List<FollowingData4> follows){
        mFollows = follows;
    }

    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.following_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowingViewHolder holder, int position) {
        final FollowingData4 followCurrent = mFollows.get(position);
        final String selectedKey = followCurrent.getKey();
        holder.nickname.setText(followCurrent.nickname);

    }
    @Override
    public int getItemCount() {
        return mFollows.size();
    }


    public class FollowingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView nickname;

        public FollowingViewHolder(View itemView){
            super(itemView);

            nickname = (TextView) itemView.findViewById(R.id.fid);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

            if (fListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    fListener.onItemClick(position);
                }
            }
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "페이지 방문");
            MenuItem delete = menu.add(Menu.NONE,2,2,"팔로잉 취소");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(fListener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()){
                        case 1:
                            fListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            fListener.onDeleteClick(position);
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
        fListener = listener;
    }
}


