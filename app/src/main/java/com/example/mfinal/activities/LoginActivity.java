package com.example.mfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mfinal.MainActivity;
import com.example.mfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.mail);
        password=findViewById(R.id.passwor);
    }

    public void Login(View v){
        String posta =email.getText().toString();
        String sifre =password.getText().toString();
        if(!posta.isEmpty()&&!sifre.isEmpty()){
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(posta,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"WRONG!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Are you sure this is correct?.", Toast.LENGTH_SHORT).show();
        }
    }
    public void Signup(View v){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        finish();
    }
}