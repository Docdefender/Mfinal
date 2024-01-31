package com.example.mfinal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mfinal.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    public void Login(View v){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
    public void Signup(View v){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        finish();
    }
}