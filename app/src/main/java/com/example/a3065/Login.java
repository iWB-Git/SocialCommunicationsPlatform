package com.example.a3065;

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

public class Login extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private TextView textforgot, signup;
    private Button BtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);
        TextView btn = findViewById(R.id.BacktoSignUp);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Mail);
        password = findViewById(R.id.Password);
        textforgot = findViewById(R.id.forgotPassword);
        signup = findViewById(R.id.BacktoSignUp);
        BtnLogin = findViewById(R.id.btnlogin);
        BtnLogin.setOnClickListener(this);
        btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.BacktoSignUp:
                startActivity(new Intent(Login.this, SignUp.class));
                break;
            case R.id.btnlogin:
                Logins();
                break;
        }
    }

    private void Logins() {

        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (user.isEmpty()) { //Warning popup for empty email box
            email.setError("Email can not be empty");
        }
        if (pass.isEmpty()) { //Warning popup for empty password box
            password.setError("Incorrect Password or can not be empty");
        } else {
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String RegisteredUserID = currentUser.getUid();
                        if(mAuth.getCurrentUser().isEmailVerified()){





                            DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
                            jLoginDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userAge = snapshot.child("accAge").getValue().toString();

                                    if (userAge.equals("New")) {
                                        Intent intentMain = new Intent(Login.this, ProfileSetup.class);
                                        startActivity(intentMain);
                                        finish();


                                    } else {
                                        Intent intentMain = new Intent(Login.this, FrameActivity.class);
                                        startActivity(intentMain);
                                        finish();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
                            jLoginDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userAge = snapshot.child("admin").getValue().toString();

                                    if (userAge.equals("admin")) {
                                        Intent intentMain = new Intent(Login.this, AdminActivity.class);
                                        startActivity(intentMain);
                                        finish();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                        }


                    }


                    else {
                        Toast.makeText(Login.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}