package com.example.a3065;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3065.chat.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class UserProfileFragment extends Fragment {
    Button follow;
    TextView name,Major;
    ImageView profile;
    ImageButton msg;

    Uri image;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    int d=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        follow=view.findViewById(R.id.follow);
        FirebaseAct act=new FirebaseAct();
        Bundle data=getArguments();
        profile=view.findViewById(R.id.img2);
        String puid,major,img;
        puid=data.getString("puid");
        Log.d("name names name", "onViewCreated: "+data.getString("name"));
        String names=data.getString("name");
        major=data.getString("major");
        img=data.getString("img");
        name=view.findViewById(R.id.uName);
        name.setText(data.getString("name"));
        ImageView imgv;


        image=Uri.parse(img);
        Log.d("imageLink", image.toString());
        Log.d("imageTest", img);
        Picasso.with(getActivity()).load(image).into(profile);
        profile=view.findViewById(R.id.profileview);
        msg=view.findViewById(R.id.msgview);
        Toast.makeText(getActivity(), puid, Toast.LENGTH_SHORT).show();
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Chat.class);
                intent.putExtra("name",data.getString("name"));
                intent.putExtra("textID","blank");
                intent.putExtra("puid",data.getString("puid"));

                DatabaseReference re=FirebaseDatabase.getInstance().getReference();
                re.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.child("Chat").getChildren()){
                            String chat=dataSnapshot.child("uniqueID").getValue().toString();
                            String[] arr=chat.split(",");

                            if(Objects.equals(arr[0], RegisteredUserID)){
                                if(d==0){
                                    intent.putExtra("textID",RegisteredUserID+","+data.getString("puid"));
                                    d=1;
                                    startActivity(intent);
                                }


                            }

                            if(Objects.equals(arr[1], RegisteredUserID)){
                                if(d==0){
                                    intent.putExtra("textID",data.getString("puid")+","+RegisteredUserID);
                                    d=1;
                                    startActivity(intent);
                                }


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(intent);

            }
        });

        imgv=view.findViewById(R.id.imageView);
        imgv.setVisibility(View.GONE);


        DatabaseReference df=FirebaseDatabase.getInstance().getReference("Users").child(puid);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("VERIFICATIONNN", "test");
                try{

                    String verify=snapshot.child("Verification").getValue().toString();

                    if(verify.equals("Verified")){
                        imgv.setVisibility(View.VISIBLE);
                    }else{
                        imgv.setVisibility(View.GONE);
                    }
                }catch(Exception e){
                    Log.d("VERIFICATIONNN", "NOT WORKING");
                    imgv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference followin= FirebaseDatabase.getInstance().getReference().child("Following").child(RegisteredUserID);
        followin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{
                    Log.d("TAG", snapshot.child(puid).getValue().toString());
                    follow.setText("un follow");


                }catch (Exception e){
                    Log.d("exception", e.toString()+"  "+puid);
                    follow.setText("follow");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("Following").child(RegisteredUserID).child(puid);
                if(follow.getText().toString().equals("un follow")){
                    dbref.removeValue();

                }else{
                    dbref.setValue(puid);
                }

            }
        });

    }
}