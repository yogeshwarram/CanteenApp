package com.google.firebase.canteenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText fullName,email,password,phone,canteenName;
    Button registerBtn,goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    RadioButton isAdmin,isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        fullName=findViewById(R.id.registerName);
        email=findViewById(R.id.registerEmail);
        password=findViewById(R.id.registerPassword);
        phone=findViewById(R.id.registerPhone);
        registerBtn=findViewById(R.id.registerBtn);
        goToLogin=findViewById(R.id.gotoLogin);
        canteenName=findViewById(R.id.registerCanteenName);

        isAdmin=findViewById(R.id.isAdmin);
        isUser = findViewById(R.id.isUser);

        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isUser.setChecked(false);
                }
            }
        });
        isUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAdmin.setChecked(false);
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);
                checkField(canteenName);

                // RadioButtons Validation
                if(!(isAdmin.isChecked() || isUser.isChecked())){
                    Toast.makeText(RegisterActivity.this,"Select the account Type",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid){
                    //Start User Registration
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this,"Account Created",Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("Email",email.getText().toString());
                            userInfo.put("PhoneNo",phone.getText().toString());
                            userInfo.put("Password",password.getText().toString());
                            userInfo.put("canteenName",canteenName.getText().toString());

                            if(isAdmin.isChecked()){
                                userInfo.put("isAdmin","1");
                            }
                            if(isUser.isChecked()){
                              //  userInfo.put("isAdmin","0");
                                userInfo.put("isUser","2");

                            }
                            df.set(userInfo);
                            if(isAdmin.isChecked()){
                                startActivity(new Intent(getApplicationContext(),OrderDetails.class));
                                finish();
                            }
                            if(isUser.isChecked()){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }
    public boolean checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Required");
            valid=false;
        }
        else{
            valid=true;
        }
        return valid;
    }

}