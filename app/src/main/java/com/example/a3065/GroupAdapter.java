package com.example.a3065;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a3065.messages.MessagesList;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {


    private List<Users> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    GroupAdapter(Context context, List<Users> data) {
        this.mContext=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public void updateData(List<Users> messagesLists){
        this.mData=messagesLists;
        notifyDataSetChanged();

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Users group = mData.get(position);
        holder.nameView.setText(group.getName());
        holder.aboutView.setText(group.getMajor());
        Picasso.with(mContext).load(group.getImgs()).into(holder.imgView);
        Log.d("SEARCH INFO", group.getName()+" "+group.getMajor());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Users getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView;
        TextView aboutView;
        ImageView imgView;

        ViewHolder(View itemView) {
            super(itemView);
            nameView=itemView.findViewById(R.id.nameview);
            aboutView=itemView.findViewById(R.id.aboutview);
            imgView=itemView.findViewById(R.id.profileview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
}

