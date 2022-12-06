package com.example.a3065.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3065.FirebaseAct;
import com.example.a3065.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Chat extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    private final List<ChatList> chatLists=new ArrayList<>();
    private ChatAdapter chatAdapter;
    private RecyclerView rv;
    String tid="blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageButton back=findViewById(R.id.backBtn);
        final TextView nameTV =findViewById(R.id.nameMsg);
        final EditText messageEDT=findViewById(R.id.sendTxt);
        final ImageView profilePic=findViewById(R.id.profilePic);
        final ImageButton sendBtn=findViewById(R.id.sendBtn);
        rv=findViewById(R.id.chattingRecycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(Chat.this));
        chatAdapter=new ChatAdapter(chatLists,Chat.this);
        rv.setAdapter(chatAdapter);


        final String getName=getIntent().getStringExtra("name");
        final String textID=getIntent().getStringExtra("textID");
        final String puid=getIntent().getStringExtra("puid");
        System.out.println(textID+"TextID");


        //MAKE SURE THAT IF TEXTID IS BLANK CREATE A NEW FIELD WITH REGID AND PUID---------
        //---------------------------------------------------------------------------------------

        //get profile pic to populate...
        nameTV.setText(getName);
        //picasso to load profile pic
        if(!(textID.equals("blank")) || !(tid.equals("blank"))){
            DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Chat");
            dref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatLists.clear();
                    for(DataSnapshot dataSnapshot : snapshot.child(textID).child("Messages").getChildren()){
                        System.out.println("first load");
                        final String msg= dataSnapshot.child("msg").getValue(String.class);
                        final String time=dataSnapshot.child("time").getValue(String.class);
                        final String sender=dataSnapshot.child("sender").getValue(String.class);
                        Timestamp timestamp=new Timestamp(Long.parseLong(time));
                        Date date=new Date(timestamp.getTime());
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                        ChatList chatList=new ChatList(sender,msg,simpleDateFormat.format(date));
                        chatLists.add(chatList);
                        rv.scrollToPosition(chatLists.size() - 1);
                        chatAdapter.updateChatList(chatLists);

                    }
                    System.out.println("first loadsasass");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        DatabaseReference re=FirebaseDatabase.getInstance().getReference();
        re.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatLists.clear();
                for(DataSnapshot dataSnapshot : snapshot.child(textID).child("Messages").getChildren()){
                    try{

                        final String msg= dataSnapshot.child("msg").getValue(String.class);
                        final String time=dataSnapshot.child("time").getValue(String.class);
                        final String sender=dataSnapshot.child("sender").getValue(String.class);
                        System.out.println(msg);System.out.println("itteratingdos");
                        Timestamp timestamp=new Timestamp(Long.parseLong(time));
                        Date date=new Date(timestamp.getTime());
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                        ChatList chatList=new ChatList(sender,msg,simpleDateFormat.format(date));
                        chatLists.add(chatList);

                        rv.scrollToPosition(chatLists.size() - 1);
                        chatAdapter.updateChatList(chatLists);
                    }catch (Exception e){

                    }




                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textID.equals("blank")){
                    tid=puid+","+RegisteredUserID;
                    DatabaseReference d=FirebaseDatabase.getInstance().getReference().child("Chat");
                    d.child(puid+","+RegisteredUserID).child("uniqueID").setValue(puid+","+RegisteredUserID);

                    FirebaseAct act=new FirebaseAct();
                    act.sendMessage( puid+","+RegisteredUserID,messageEDT.getText().toString());
                    messageEDT.setText("");
                    chatAdapter.updateChatList(chatLists);

                }else{
                    FirebaseAct act=new FirebaseAct();
                    act.sendMessage( textID,messageEDT.getText().toString());
                    messageEDT.setText("");
                    chatAdapter.updateChatList(chatLists);
                }
                for(int i=0;i<chatLists.size();i++){
                    System.out.println("\u001B[43m"+"Chat Lists Are "+chatLists.get(i).getMessage());

                }





            }

        });


    }

}