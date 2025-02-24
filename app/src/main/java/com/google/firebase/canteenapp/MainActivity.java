package com.google.firebase.canteenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import android.util.Log;


import java.util.ArrayList;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ItemAdapter mItemAdapter;
    private ListView mItemListView;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mItemDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String currentUserName;
    private DatabaseReference mOrderDatabaseReference;
    private FirebaseFirestore db;
    private String mName;
    private String currentUserId;
    private String canteenName;
    private String canteen;
    private TextView emptyText;
    private DrawerLayout drawer;
    private static final int RC_SIGN_IN = 1;
    ArrayList<Items> items= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        canteen = intent.getStringExtra("canteenName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setTitle(canteen+" Canteen");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        currentUserId=user.getUid();
        mItemListView=(ListView)findViewById(R.id.itemListView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("items").child(canteen);
        mFirebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);
        final TextView orderButton=(TextView)findViewById(R.id.confirmOrderButton);
        TextView placeOrderButton=(TextView)findViewById(R.id.placeOrderButton);
        final TextView quantityTextView=(TextView)findViewById(R.id.quantity_text_view);

        mItemAdapter=new ItemAdapter(this,R.layout.list_item,items);
        mItemListView.setAdapter(mItemAdapter);


        DocumentReference docRef = db.collection("Users").document(currentUserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document=task.getResult();
                currentUserName=document.getString("FullName");
                canteenName=document.getString("canteenName");
                mOrderDatabaseReference=mFirebaseDatabase.getReference("orders").child(canteenName).child(currentUserName);

            }
        });

        //ConfirmOrder button Listener
      orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total=calculateOrderTotal();
                Toast.makeText(MainActivity.this,Integer.toString(total),Toast.LENGTH_SHORT).show();
                String details="Total = "+Integer.toString(total);
                orderButton.setText(details);
            }
        });

      placeOrderButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              int count=0;
              for (Items item : items){
                  if (item.getmQuantity()!=0){
                      count+=1;
                        mOrderDatabaseReference.push().setValue(item);

                  }
              }
              if (count==0){
                  Toast.makeText(MainActivity.this,"Please add items",Toast.LENGTH_SHORT).show();
              }
              else {
                  Toast.makeText(MainActivity.this,"Your Order has been placed",Toast.LENGTH_SHORT).show();
                  orderButton.setText("Confirm Order");
                  Intent placeOrderIntent= new Intent(getApplicationContext(), PaymentActivity.class);
                  startActivity(placeOrderIntent);
              }

          }
      });



      //THis is the auth state listener which will show login menu if user is not logged in
 /**       mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    //signedin
                    currentUserName=user.getDisplayName();
                    mOrderDatabaseReference=mFirebaseDatabase.getReference("orders").child(currentUserName);
                    attachDatabaseReadListener();
                    Toast.makeText(MainActivity.this,"You are signed in.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
  **/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();
                Intent signOutIntent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(signOutIntent);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //calculating total of all the selected items
    public int calculateOrderTotal(){
        int orderTotal=0;
        for (Items item : items){

            orderTotal+=item.getmQuantity()*Integer.parseInt(item.getPrice());
        }


        return orderTotal;
    }

    //This will help to read data from firebase database
    public void attachDatabaseReadListener(){
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Items mItem= snapshot.getValue(Items.class);
                mItemAdapter.add(mItem);
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

    //This will remove all the items from the list once loggedout
    private void detachDatabaseLisener(){
        if (mChildEventListener!=null) {
            mItemDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //This is for the option menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();
                Intent signOutIntent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(signOutIntent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
*/
    @Override
    protected void onPause() {
        super.onPause();
      //  mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        detachDatabaseLisener();
        mItemAdapter.clear();
        finish();

    }
    @Override
    protected void onResume() {
        super.onResume();

     //   mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        attachDatabaseReadListener();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mItemListView.setEmptyView(emptyText);

            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==RC_SIGN_IN){
            if (resultCode==RESULT_OK){
                Toast.makeText(this,"Signed in!",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode==RESULT_CANCELED){
                Toast.makeText(this,"Sign in canceled",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
