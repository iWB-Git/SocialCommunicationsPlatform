package com.example.a3065;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3065.messages.MessageAdapter;
import com.example.a3065.messages.MessagesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class chatFragment extends Fragment {
    private String name,email,uid;
    private TextView last;
    private RecyclerView messagesRecyclerView;
    String RegisteredUserID= FirebaseAuth.getInstance().getUid();
    private FirebaseAuth mAuth;
    private MessageAdapter messageAdapter;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    private final List<MessagesList> messagesList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messagesRecyclerView = view.findViewById(R.id.result_list);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        last=view.findViewById(R.id.lastmessage);

        messageAdapter=new MessageAdapter(messagesList,getActivity());

        messagesRecyclerView.setAdapter(messageAdapter);


        FirebaseAct act=new FirebaseAct();



        DatabaseReference re=FirebaseDatabase.getInstance().getReference();
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Chat").getChildren()){
                    String chat=dataSnapshot.child("uniqueID").getValue().toString();
                    String[] arr=chat.split(",");
                    boolean one=false,two=false;
                    String img;


                    if(Objects.equals(arr[0], RegisteredUserID)){
                        one=true;
                    }

                    if(Objects.equals(arr[1], RegisteredUserID)){
                        two=true;
                    }

                    if(one||two){
                        String reference="";
                        if(one){
                            reference=arr[1];
                        }if(two) {
                            reference = arr[0];
                        }



                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(reference);
                        ref.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String rets=snapshot.child("name").getValue().toString();
                                final ArrayList<Users> users = new ArrayList<>();
                                Iterator iterator = snapshot.getChildren().iterator();
                                String lastMessage="";
                                int unseenMessages=0;
                                String uid=snapshot.child("uid").getValue().toString();
                                String profilePics=snapshot.child("images").child(uid).getValue().toString();
                                lastMessage="";



                                ref.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        boolean a=false;
                                        int index=0;
                                        if(!(messagesList.isEmpty())){
                                            for(int i=0;i<messagesList.size();i++){
                                                if(rets.equals(messagesList.get(i).getName())){
                                                    a=true;
                                                    index=i;
                                                    messagesList.remove(index);
                                                    break;

                                                }
                                            }

                                        }

                                            MessagesList msg=new MessagesList(rets,chat,dataSnapshot.child("lastMsg").getValue(String.class),profilePics,unseenMessages);
                                            messagesList.add(msg);
                                            messageAdapter.updateData(messagesList);





                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        messagesList.clear();
                        messageAdapter.updateData(messagesList);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}