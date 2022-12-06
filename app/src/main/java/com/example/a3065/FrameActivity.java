package com.example.a3065;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a3065.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FrameActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private ImageView ig;
    private ImageButton menu;
    FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ig=findViewById(R.id.img2);
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu =new PopupMenu(FrameActivity.this,v);
                popupMenu.inflate(R.menu.profile_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.Requestv:
                                requestVerification();
                                break;
                            case R.id.UpdateProfile:
                                Intent intent=new Intent(FrameActivity.this,ProfileSetup.class);
                                startActivity(intent);
                                break;
                            case R.id.Logout:
                                FirebaseAuth.getInstance().signOut();
                                Intent inte=new Intent(FrameActivity.this,Login.class);
                                startActivity(inte);
                                break;

                        }
                        return true;
                    }
                });
            }
        });

        replaceFragment(new allFragment());




        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                switch (item.getItemId()){
                    case R.id.homeIcon:
                        replaceFragment(new allFragment());
                        break;
                    case R.id.profileIcon:
                        replaceFragment(new homeFragment());
                        break;
                    case R.id.chatIcon:
                        replaceFragment(new chatFragment());
                        break;
                }
            return true;
        });
    }
    private void requestVerification(){
        FirebaseUser users = currentUser;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Verification").child(uid);
        VerifyUser verify=new VerifyUser(uid,users.getEmail(),users.getDisplayName());
        ref.setValue(verify);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}