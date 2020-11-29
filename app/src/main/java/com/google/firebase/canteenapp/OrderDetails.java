package com.google.firebase.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {
    private DatabaseReference mOrderDatabaseReference;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mItemDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private OrdersAdapter mOrdersAdapter;
    private FirebaseFirestore db;
    private TextView emptyText;
    private ListView listView;
    private String canteen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

   //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent1=getIntent();
        canteen= intent1.getStringExtra("canteenName");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("orders").child(canteen);
        db=FirebaseFirestore.getInstance();

        listView = (ListView) findViewById(R.id.orderListView);
        final ArrayList<Orders> users=new ArrayList<>();
        emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);



        mOrdersAdapter=new OrdersAdapter(this,R.layout.orderlistview,users);
        listView.setAdapter(mOrdersAdapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),CompleteDetails.class);
             //   String itemValue = (String) listView.getItemAtPosition(i);
                Orders mOrder=(Orders)listView.getItemAtPosition(i);
                String itemValue=mOrder.getName();
                intent.putExtra("username",itemValue);
                intent.putExtra("canteenName",canteen);
               // mFirebaseDatabase.getReference("orders").child(itemValue).removeValue();
               startActivity(intent);
            }
        });



    }

    public void attachDatabaseListener(){
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                final String name= snapshot.getKey();

                //below statements will extract user's full name using uid which we obtained in above statement

                mOrdersAdapter.add(new Orders(name,canteen));
                //  String message= "Order Details of \n"+name;

                //

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
         /**   case android.R.id.home:
                //Write your logic here
                Intent intent=new Intent(OrderDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
          **/

            case R.id.add_item_menu:
                //To load add_item.xml file
                Intent intent1 = new Intent(this, AddItem.class);
                startActivity(intent1);
                return true;
            case R.id.delete_items:
                Intent deleteItems = new Intent(this, DeleteItems.class);
                deleteItems.putExtra("canteenName",canteen);
                startActivity(deleteItems);
                return true;
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();
                Intent signOutIntent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(signOutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mOrdersAdapter.clear();
    }
    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseListener();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setEmptyView(emptyText);
                emptyText.setVisibility(View. VISIBLE);
            }
        }, 2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    //This is for the option menu
    }