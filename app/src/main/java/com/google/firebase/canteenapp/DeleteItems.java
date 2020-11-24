package com.google.firebase.canteenapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DeleteItems extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_items);
        ArrayList<String> deleteItems=new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
       // Button deleteButton=(Button)findViewById(R.id.delete_btn);
       // deleteButton.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, R.layout.orderlistview, R.id.textView, deleteItems);
        final ListView listView = (ListView) findViewById(R.id.deleteListView);
        listView.setAdapter(itemsAdapter);

        deleteItems.add("hello");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //     mFirebaseDatabase.getReference("orders").child(orders.getName()).removeValue();
            //    listView.remove(position); //or some other task

            }
        });
    }
}
