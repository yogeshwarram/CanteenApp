package com.google.firebase.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddItem extends AppCompatActivity {
    private Button mSendButton;
    private EditText mNameText;
    private EditText mPriceText;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        mSendButton=(Button)findViewById(R.id.button);
        mNameText=(EditText)findViewById(R.id.enterName);
        mPriceText=(EditText)findViewById(R.id.enterPrice);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference("items");

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send items to server on click
                Items mItem = new Items(0,mNameText.getText().toString(),mPriceText.getText().toString());
                mItemDatabaseReference.push().setValue(mItem);
             //   db.collection("items").document(mNameText.getText().toString()).set(mItem);
                // Clear input box
                Toast.makeText(getApplicationContext(),"Item Added Successfully",Toast.LENGTH_SHORT).show();
                mNameText.setText("");
                mPriceText.setText("");
            }
        });
    }
    //Back button in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                Intent intent=new Intent(AddItem.this, OrderDetails.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
