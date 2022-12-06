package com.example.a3065;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText username, password,email,password2;
    private TextView HaveAccount;
    private Button BtnSignUp;
    private Button Button3;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_test);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent i = new Intent(SignUp.this, FrameActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d("TAG", "onAuthStateChanged:signed_out");
        }

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.Mail);
        password = findViewById(R.id.Password);
        HaveAccount = findViewById(R.id.HaveAccount);
        BtnSignUp = findViewById(R.id.SignUp);
        password2=findViewById(R.id.ConfirmPassword);
        BtnSignUp.setOnClickListener(this);
        HaveAccount.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.SignUp:
                registerUser();
                break;
            case R.id.HaveAccount:
                startActivity(new Intent(SignUp.this, Login.class));
                break;
        }

    }

    private void registerUser() {
        String emails = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String pass2=password2.getText().toString().trim();

        if(emails.isEmpty()){
            email.setError("Email is Required !");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
            email.setError("Please Enter a Valid Email Address !");
            email.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            Toast.makeText(SignUp.this,pass,Toast.LENGTH_LONG).show();
            password.setError("Password is Required!");
            password.requestFocus();
            return;
        }
        if(pass.length() < 8){
            password.setError("Min Password Length should be 8 Characters !");
            password.requestFocus();
            return;
        }
        if(pass2.isEmpty()){
            password2.setError("Please Confirm Your Password  !");
            password2.requestFocus();
            return;
        }
        if(!pass.equals(pass2)){
            password2.requestFocus();
            password2.requestFocus();
            Toast.makeText(SignUp.this,"Passwords do not match",Toast.LENGTH_LONG).show();
            return;
        }
        if(!(emails.endsWith("@usiu.ac.ke"))){
            password.setError("Please use your university email !");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emails, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(emails,"New");
                            FirebaseUser users = mAuth.getCurrentUser();

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this,"Check your email to verify",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Intent intentMain = new Intent(SignUp.this, Login.class);
                                                startActivity(intentMain);
                                                finish();
                                            }else{
                                                Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(SignUp.this,"failure",Toast.LENGTH_LONG).show();
                        }



                    }
                });


    }}