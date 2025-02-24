package com.google.firebase.canteenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button loginBtn,gotoRegister;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private ProgressBar pb;
    Intent intent1;
    Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);
        pb = (ProgressBar) findViewById(R.id.loading_indicator);
        intent1=new Intent(getApplicationContext(),OrderDetails.class);
        intent2=new Intent(getApplicationContext(),MainActivity.class);
        // pb.setVisibility(ProgressBar.INVISIBLE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkField(email);
                checkField(password);
                pb.setVisibility(ProgressBar.VISIBLE);
                if(valid){
                        fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this,"Log in Successful",Toast.LENGTH_SHORT).show();
                                checkUserAccessLevel(authResult.getUser().getUid());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pb.setVisibility(ProgressBar.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Login Failed! Please try again",Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });


        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        //Extract Data from Document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("Tag","onSuccess"+documentSnapshot.getData());
                //Identifying User
                if(documentSnapshot.getString("isAdmin")!=null){
                    // user is admin
                    String canteen=documentSnapshot.getString("canteenName");

                    intent1.putExtra("canteenName",canteen);
                    startActivity(intent1);
                    finish();
                }
                if(documentSnapshot.getString("isUser")!=null){
                    // user is customer
                    String canteen= documentSnapshot.getString("canteenName");
                    intent2.putExtra("canteenName",canteen);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Required");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        pb = (ProgressBar) findViewById(R.id.loading_indicator);
        pb.setVisibility(ProgressBar.VISIBLE);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isAdmin")!=null){
                        // user is admin
                        String canteen= documentSnapshot.getString("canteenName");
                        intent1.putExtra("canteenName",canteen);
                        startActivity(intent1);
                        finish();
                    }
                    if(documentSnapshot.getString("isUser")!=null){
                        // user is customer
                        String canteen= documentSnapshot.getString("canteenName");
                        intent2.putExtra("canteenName",canteen);
                        startActivity(intent2);
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            });
        }
        else {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}