package com.google.firebase.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DeleteItems extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    ItemAdapter mItemAdapter;
    private ListView listView;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mItemDatabaseReference;
    private ArrayList<String> keysList = new ArrayList<>();
    private ArrayList<Items> deleteItems=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_items);

        Intent i=getIntent();
        String canteen=i.getStringExtra("canteenName");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
       // Button deleteButton=(Button)findViewById(R.id.delete_btn);
       // deleteButton.setVisibility(View.INVISIBLE);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("items").child(canteen);
        listView = (ListView) findViewById(R.id.deleteListView);
        mItemAdapter=new ItemAdapter(this,R.layout.list_item,deleteItems);
        listView.setAdapter(mItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //     mFirebaseDatabase.getReference("orders").child(orders.getName()).removeValue();
            //    listView.remove(position); //or some other task
                String key = keysList.get(position);
                mItemDatabaseReference.child(key).removeValue();
                Toast.makeText(getApplicationContext(),"Item has been removed",Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void attachDatabaseReadListener(){
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Items mItem= snapshot.getValue(Items.class);
                keysList.add(snapshot.getKey());
                mItemAdapter.add(mItem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Items item = snapshot.getValue(Items.class);
                deleteItems.remove(item);
                keysList.remove(snapshot.getKey());
                mItemAdapter.notifyDataSetChanged();
                finish();
                startActivity(getIntent());
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
    protected void onResume() {
        super.onResume();
        //   mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        attachDatabaseReadListener();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                //This will directly go to the login page because loginactivity will pass the intent for the user's canteen name
                Intent intent=new Intent(DeleteItems.this, LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
