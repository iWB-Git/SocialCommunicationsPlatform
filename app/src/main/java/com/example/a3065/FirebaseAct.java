package com.example.a3065;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3065.messages.MessagesList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.ResponseCache;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class FirebaseAct {
    FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    DatabaseReference mData=FirebaseDatabase.getInstance().getReference("Users").child(RegisteredUserID).child("images");
    StorageReference reference= FirebaseStorage.getInstance().getReference().child("ProfileImages");
    Boolean check=false;
    private String returns;

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public String getReturns() {
        return returns;
    }

    public void updateValue(Object obj){
        FirebaseDatabase.getInstance().getReference("Users").child(RegisteredUserID)
                .setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }else{
                            Log.d("TAG", "onFail: ");
                        }
                    }
                });
    }

    public void newChat(String puid){

        DatabaseReference re=FirebaseDatabase.getInstance().getReference().child("Chat");

    }
    public void sendMessage(String chatKey,String message){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Chat").child(chatKey).child("Messages");
        DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Chat").child(chatKey);
        final String time=String.valueOf(System.currentTimeMillis());
        ref.child(time).child("sender").setValue(RegisteredUserID);
        ref.child(time).child("msg").setValue(message);
        ref.child(time).child("time").setValue(time);
        dref.child("lastMsg").setValue(message);


    }






    public void updateImage(String ext,Uri uri,FirebaseUser users){
        StorageReference ref=reference.child(RegisteredUserID+"."+ext);
        ref.putFile(uri)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("PhotoURI", uri.toString());
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                                        .setPhotoUri(uri)
                                        .build();
                                users.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });
                                mData.child(RegisteredUserID).setValue(uri.toString());
                            }
                        });







                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress=(100*snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                        Log.d("Download ", Double.toString(progress));
                    }
                });



    }
    public void message(String puid,String message){
        DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Chat").child(RegisteredUserID+","+puid);
        Date date = new Date();
        long timeMilli = date.getTime();
        Log.d("time on send", "message: "+timeMilli);
        String key=dref.push().getKey();
        Message msg=new Message(RegisteredUserID,puid,message,Long.toString(timeMilli));
        dref.child(key).setValue(msg);
    }


}
