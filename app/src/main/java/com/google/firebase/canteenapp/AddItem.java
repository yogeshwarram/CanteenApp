package com.google.firebase.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {
    private Button mSendButton;
    private EditText mNameText;
    private EditText mPriceText;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        Intent intent = getIntent();
        mSendButton=(Button)findViewById(R.id.button);
        mNameText=(EditText)findViewById(R.id.enterName);
        mPriceText=(EditText)findViewById(R.id.enterPrice);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("items");

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send items to server on click
                Items mItem = new Items(0,mNameText.getText().toString(),mPriceText.getText().toString());
                mItemDatabaseReference.push().setValue(mItem);
                // Clear input box
                mNameText.setText("");
                mPriceText.setText("");
            }
        });
    }
}
