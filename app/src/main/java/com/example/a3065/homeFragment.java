package com.example.a3065;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.provider.FirebaseInitProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class homeFragment extends Fragment {
    String uid=FirebaseAuth.getInstance().getUid();
    private FirebaseAuth mAuth;
    private TextView name,about;
    private ImageView imgv;


    private ImageView img2,menu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("images");





        return inflater.inflate(R.layout.fragment_home, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img2=view.findViewById(R.id.img2);
        mAuth = FirebaseAuth.getInstance();
        name=view.findViewById(R.id.uName);
        imgv=view.findViewById(R.id.imageView);
        imgv.setVisibility(View.INVISIBLE);
        about=view.findViewById(R.id.About);
        try{
            DatabaseReference df=FirebaseDatabase.getInstance().getReference("Users").child(uid);
            df.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        String verify=snapshot.child("Verification").getValue().toString();
                        if(verify.equals("Verified")){
                            imgv.setVisibility(View.VISIBLE);
                        }else{
                            imgv.setVisibility(View.INVISIBLE);
                        }
                    }catch(Exception e){
                        imgv.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch(Exception e){
            imgv.setVisibility(View.INVISIBLE);

        }

        FirebaseUser users = mAuth.getCurrentUser();
        try{
            DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{

                        String names=snapshot.child("name").getValue().toString();
                        name.setText(names);
                        String abouts=snapshot.child("about").getValue().toString();
                        about.setText(abouts);

                    }catch(Exception ex){

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){

        }

        if(users.getPhotoUrl()!=null){

            Toast.makeText(getActivity(), users.toString(), Toast.LENGTH_SHORT).show();
            Picasso.with(getActivity()).load(users.getPhotoUrl()).into(img2);
        }
    }

}