package com.example.a3065.messages;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a3065.R;
import com.example.a3065.chat.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<MessagesList> messagesLists;
    private final Context context;


    public MessageAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessagesList list2= messagesLists.get(position);
        if(!(list2.getProfilePic().isEmpty())){
            Picasso.with(context.getApplicationContext()).load(list2.getProfilePic()).into(holder.profilePic);
        }
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());
        if(list2.getUnseenMessages()==0){
            holder.unseenMessage.setVisibility(View.GONE);
        }else{
            holder.unseenMessage.setVisibility(View.VISIBLE);
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Chat.class);
                intent.putExtra("name",list2.getName());
                intent.putExtra("textID",list2.getUid());
                context.startActivity(intent);
            }
        });

    }
    public void updateData(List<MessagesList> messagesLists){
        this.messagesLists=messagesLists;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView profilePic;
        private TextView name,lastMessage,unseenMessage;
        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            profilePic=itemView.findViewById(R.id.profilePic);
            name=itemView.findViewById(R.id.nameMsg);
            lastMessage=itemView.findViewById(R.id.lastMsg);
            unseenMessage=itemView.findViewById(R.id.unseenMessages);
            rootLayout=itemView.findViewById(R.id.rootLayout);

        }
    }

}
