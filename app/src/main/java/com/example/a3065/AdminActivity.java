package com.example.a3065;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class AdminActivity extends AppCompatActivity {

    GroupAdapter adapter;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    RecyclerView rv;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        rv=findViewById(R.id.recyclerAdmin);
        dialog=new Dialog(this);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Verification");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<Users> users = new ArrayList<>();
                Iterator iterator = snapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    DataSnapshot snap = (DataSnapshot) iterator.next();

                    String uid=snap.child("uid").getValue().toString();
                    String uname=snap.child("name").getValue().toString();
                    String email=snap.child("email").getValue().toString();
                    AdminVerify user=new AdminVerify(uid,uname,email);
                    Log.d("uid and usrid", user.getUid());


                    DatabaseReference re= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    re.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Users usr=snapshot.getValue(Users.class);
                                DatabaseReference refs=FirebaseDatabase.getInstance().getReference().child("Users").child(usr.getUID()).child("images");
                                refs.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String img=snapshot.child(usr.getUID()).getValue().toString();
                                        usr.setImg(img);
                                        Log.d("image", usr.getImg().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                if(usr.getUID().equals(user.getUid())){

                                    users.add(usr);
                                }
                                adapter.updateData(users);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                    try{
                        rv.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
                        adapter=new GroupAdapter(AdminActivity.this,users);
                        rv.setAdapter(adapter);
                        rv.smoothScrollToPosition(0);
                        adapter.setClickListener(new GroupAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                openVerifiedDialog(adapter.getItem(position).getUID(),adapter.getItem(position).getName(),adapter.getItem(position).getEmail(),adapter.getItem(position).getImg());


                            }
                        });
                    }catch (Exception e){

                    }


                }
                adapter.updateData(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openVerifiedDialog(String uid, String name, String email, Uri img) {
        dialog.setContentView(R.layout.verified_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageClose=dialog.findViewById(R.id.imageClose);
        TextView emails=dialog.findViewById(R.id.textView3);
        emails.setText(email);
        TextView names=dialog.findViewById(R.id.textView);
        names.setText(name);
        ImageView imgs=dialog.findViewById(R.id.imageView3);
        Picasso.with(this).load(img).into(imgs);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnVerify=dialog.findViewById(R.id.button);
        Button btnCancle=dialog.findViewById(R.id.cancel);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Verification")
                        .setValue("Verified");
                FirebaseDatabase.getInstance().getReference("Verification").child(uid)
                        .removeValue();
                dialog.dismiss();

            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Verification").child(uid)
                        .removeValue();
                dialog.dismiss();

            }
        });
        dialog.show();
    }
}