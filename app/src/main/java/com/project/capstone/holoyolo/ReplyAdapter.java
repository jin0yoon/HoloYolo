package com.example.user.holoyolo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private List<ReplyModel> mReplys;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    private OnItemClickListener rListener;
    final String id = FirebaseInstanceId.getInstance().getId();

    public ReplyAdapter(List<ReplyModel> replys){
        mReplys = replys;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReplyViewHolder holder, int position) {
        final ReplyModel replyCurrent = mReplys.get(position);
        final String selectedKey =  replyCurrent.getKey();
        holder.commentid.setText(replyCurrent.nickname);
        holder.commentview.setText(replyCurrent.contents);

    }

    @Override
    public int getItemCount() {
        return mReplys.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        TextView commentview;
        TextView commentid;

        public ReplyViewHolder(View itemView) {
            super(itemView);

            commentview = (TextView) itemView.findViewById(R.id.comment_view);
            commentid = (TextView) itemView.findViewById(R.id.comment_id);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if(rListener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    rListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(rListener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()){
                        case 1:
                            rListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            rListener.onDeleteClick(position);
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
        rListener = listener;
    }
}
