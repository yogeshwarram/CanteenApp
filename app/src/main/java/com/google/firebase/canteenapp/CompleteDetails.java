package com.google.firebase.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.DistributionOrBuilder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CompleteDetails extends AppCompatActivity {

    ListView mItemListView;
    private ItemAdapter mItemAdapter;
    private DatabaseReference mItemDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
    ArrayList<Items> items= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        TextView textView=(TextView)findViewById(R.id.order_textview);
        textView.setText("Order Details of user "+username);
        final ListView listView = (ListView) findViewById(R.id.testView);
        final ArrayList<String> users=new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.orderlistview, R.id.textView, users);
        listView.setAdapter(arrayAdapter);

      //  Button deleteButton=(Button)findViewById(R.id.delete_btn);
       // deleteButton.setVisibility(View.INVISIBLE);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference("orders").child(username);


/**
        mItemListView=(ListView)findViewById(R.id.testView);
        mItemAdapter=new ItemAdapter(this,R.layout.list_item,items);
        mItemListView.setAdapter(mItemAdapter);
        **/

        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               // Items mItem= snapshot.getValue(Items.class);
                String name = snapshot.child("name").getValue(String.class);
                int quantity = snapshot.child("mQuantity").getValue(Integer.class);
                String message = "Item Name : "+name+"\nTotal Quantity : "+Integer.toString(quantity);
                arrayAdapter.add(message);
                //mItemAdapter.add(mItem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mItemDatabaseReference.addChildEventListener(mChildEventListener);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                Intent intent=new Intent(CompleteDetails.this, OrderDetails.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
