package com.example.a3065;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileSetup extends AppCompatActivity implements View.OnClickListener {
    public static final int PICK_IMAGE_REQUEST=1;
    private Uri mImage;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    private TextView newname,fullname,major,abouts;
    private ImageView update,mains;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        Button send=findViewById(R.id.signUp);
        newname=findViewById(R.id.username);
        fullname=findViewById(R.id.username2);
        major=findViewById(R.id.username3);
        abouts=findViewById(R.id.username4);
        update=findViewById(R.id.edtImg);
        mains=findViewById(R.id.img2);
        update.setOnClickListener(this);

        send.setOnClickListener(this);
        updateIntent();
        //SETUP USE PROFILE ON FIRST LOGIN

    }
    public void updateIntent(){
        try{
            String uname=currentUser.getDisplayName();
            newname.setText(uname);
            Uri img=currentUser.getPhotoUrl();
            Picasso.with(this).load(img).into(mains);
            DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        String name=snapshot.child("name").getValue().toString();
                        fullname.setText(name);
                        String about =snapshot.child("about").getValue().toString();
                        abouts.setText(about);

                        String majors =snapshot.child("major").getValue().toString();
                        major.setText(majors);
                    }catch(Exception ex){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edtImg:
                uploadImage();
                break;

            case R.id.signUp:
                sendData();
                break;


        }

    }

    private void uploadImage() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
            && data != null && data.getData()!=null){
            mImage=data.getData();
            updateImage();

        }
    }

    private void updateImage() {
        Picasso.with(this).load(mImage).into(mains);
    }

    public void sendData(){
        FirebaseUser users = mAuth.getCurrentUser();
        if(mImage!=null){

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newname.getText().toString())
                    .build();
            users.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                        }
                    });
            Toast.makeText(ProfileSetup.this, mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        //NOW ADD DATA TO REALTIME DATABASE AND FIRESTORE

        FirebaseAct act=new FirebaseAct();
        Users user=new Users(fullname.getText().toString(),major.getText().toString(),RegisteredUserID,"Old",newname.getText().toString(),users.getEmail(),abouts.getText().toString());
        act.updateValue(user);
        act.updateImage(getExtention(mImage),mImage,users);

        DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    String verify=snapshot.child("Verification").getValue().toString();
                    if(verify.equals("Verified")){
                        FirebaseDatabase.getInstance().getReference("Users").child(RegisteredUserID).child("Verification")
                                .setValue("Verified");
                    }
                }catch(Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










        Intent intent=new Intent(this, FrameActivity.class);
        startActivity(intent);



    }
    private String getExtention(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}