package com.example.a3065;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    TextView name,major,abt;
    ImageView img;
    Button follow;
    ImageView imgv;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        String puid=intent.getStringExtra("UID");
        name=findViewById(R.id.uName);
        major=findViewById(R.id.uMajor);
        follow=findViewById(R.id.follow);
        img=findViewById(R.id.img2);
        imgv=findViewById(R.id.imageView);
        imgv.setVisibility(View.GONE);
        abt=findViewById(R.id.About);


            DatabaseReference df=FirebaseDatabase.getInstance().getReference("Users").child(puid);
            df.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("VERIFICATIONNN", "test");
                    try{

                        String verify=snapshot.child("Verification").getValue().toString();
                        String about=snapshot.child("about").getValue().toString();
                        abt.setText(about);

                        if(verify.equals("Verified")){
//                            imgv.setVisibility(View.VISIBLE);
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




        DatabaseReference followin= FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID).child("following");
        followin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{
                    Log.d("TAG", snapshot.child(puid).getValue().toString());
                    follow.setText("un follow");
                    return;


                }catch (Exception e){
                    Log.d("exception", e.toString()+"  "+puid);
                    follow.setText("follow");
                    return;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID).child("following").child(puid);
                if(follow.getText().toString().equals("un follow")){
                    dbref.removeValue();
                    onBackPressed();

                }else{
                    dbref.setValue(puid);
                }

            }
        });

    }

}