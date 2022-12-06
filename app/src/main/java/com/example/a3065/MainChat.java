package com.example.a3065;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainChat extends AppCompatActivity {
    Intent i=getIntent();
    String puid=i.getStringExtra("puid");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
    }
}