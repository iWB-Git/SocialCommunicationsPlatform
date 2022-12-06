package com.example.a3065;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a3065.messages.MessagesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class allFragment extends Fragment {
    private EditText searchTxt;
    private ImageButton searchBtn;
    private RecyclerView resultList;
    private FirebaseAuth mAuth;

    GroupAdapter adapter;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();
    private static Context mContext;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        searchTxt=view.findViewById(R.id.searchtxt);
        searchTxt=view.findViewById(R.id.searchtxt);
        searchBtn=view.findViewById(R.id.searchbtn);
        resultList=view.findViewById(R.id.result_list);
        mContext = getActivity();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultList.smoothScrollToPosition(0);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final ArrayList<Users> users = new ArrayList<>();
                        Iterator iterator = snapshot.getChildren().iterator();

                        while(iterator.hasNext()){
                            DataSnapshot snap = (DataSnapshot) iterator.next();
                            Users user=snap.getValue(Users.class);
                            String uname=user.getName();
                            DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID).child("images");
                            df.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try{


                                        Log.d("usersssss", user.getUID());
                                        DatabaseReference refs=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUID()).child("images");
                                        refs.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String img=snapshot.child(user.getUID()).getValue().toString();
                                                user.setImg(img);
                                                if(user.getUsername().startsWith(searchTxt.getText().toString()))
                                                    users.add(user);
                                                resultList.smoothScrollToPosition(0);
                                                adapter.updateData(users);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                    }catch(Exception e){

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            try{
                                resultList.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter=new GroupAdapter(getActivity(),users);
                                resultList.setAdapter(adapter);
                                resultList.smoothScrollToPosition(0);
                                adapter.setClickListener(new GroupAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Toast.makeText(getApplicationContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getActivity(), UserProfile.class);

                                        i.putExtra("UID", adapter.getItem(position).getUID());
//                                        startActivity(i);
                                        Log.d("UIDS", adapter.getItem(position).getUID());
                                        Fragment prof=new UserProfileFragment();


                                        FragmentTransaction fm=getActivity().getSupportFragmentManager().beginTransaction();
                                        Bundle data=new Bundle();
                                        Log.d("adapter text", "onItemClick: "+adapter.getItem(position).getName());
                                        data.putString("puid",adapter.getItem(position).getUID());
                                        data.putString("name",adapter.getItem(position).getName());
                                        data.putString("major",adapter.getItem(position).getMajor());
                                        data.putString("img",adapter.getItem(position).getImg().toString());
                                        prof.setArguments(data);
                                        getActivity().getFragmentManager().popBackStack();
                                        onDestroy();
                                        fm.remove(allFragment.this).replace(R.id.frameLayout,prof).commit();
                                    }
                                });
                            }catch (Exception e){

                            }



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

            }
            /////////



        });

    }

    private void firebaseUserSearch() {


    }

    //View Holder  Class
    public class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

        }





    }
    public Context getContext() {
        return mContext;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        getFragmentManager().beginTransaction().remove((Fragment) allFragment.this).commitAllowingStateLoss();

    }

}