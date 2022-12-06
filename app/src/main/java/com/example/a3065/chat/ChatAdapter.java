package com.example.a3065.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a3065.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private  List<ChatList> chatLists;
    private final Context context;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    public ChatAdapter(List chatLists, Context context){
        this.chatLists=chatLists;
        this.context=context;
    }



    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatList list2= chatLists.get(position);

        if(list2.getSender().equals(RegisteredUserID)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.opsLayout.setVisibility(View.GONE);
            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate());
        }else{
            holder.myLayout.setVisibility(View.GONE);
            holder.opsLayout.setVisibility(View.VISIBLE);
            holder.opsMessage.setText(list2.getMessage());
            holder.opsTime.setText(list2.getDate());
        }


    }
    public void updateChatList(List<ChatList> chatLists){
        this.chatLists=chatLists;
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout opsLayout,myLayout;
        private TextView opsMessage, myMessage;
        private TextView opsTime, myTime;

        private TextView txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            opsLayout=itemView.findViewById(R.id.opsLayout);
            myLayout=itemView.findViewById(R.id.myLayout);
            opsMessage=itemView.findViewById(R.id.opsMessage);
            myMessage=itemView.findViewById(R.id.myMessage);
            opsTime=itemView.findViewById(R.id.opsMsgTime);
            myTime=itemView.findViewById(R.id.myMsgTime);
        }
    }
}