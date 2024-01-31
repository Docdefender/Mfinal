package com.example.mfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mfinal.R;
import com.example.mfinal.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText firstname,lastname,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

    }
    public void Login(View v){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
    public void Signup(View v){
        String isim =firstname.getText().toString();
        String soyisim =lastname.getText().toString();
        String posta =email.getText().toString();
        String sifre =password.getText().toString();

        if(!isim.isEmpty() && !soyisim.isEmpty() && !posta.isEmpty() && !sifre.isEmpty()){
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(posta,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String uid = task.getResult().getUser().getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Users user = new Users(isim,soyisim,posta);
                        db.collection("Users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Signup successful",Toast.LENGTH_SHORT).show();
                                startActivity( new Intent(getApplicationContext(),LoginActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Signup not successful",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Something is wrong!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}