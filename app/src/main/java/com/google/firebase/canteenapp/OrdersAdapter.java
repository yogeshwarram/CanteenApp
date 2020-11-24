package com.google.firebase.canteenapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends ArrayAdapter<Orders> {
    private FirebaseDatabase mFirebaseDatabase;
    private ArrayList<Orders> list = new ArrayList<Orders>();
    private Context context;
    public OrdersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Orders> objects) {
        super(context, resource, objects);

        this.list=objects;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.orderlistview, parent, false);
        }

        TextView nameTextView=(TextView)convertView.findViewById(R.id.textView);
        final Orders orders=getItem(position);
        nameTextView.setText(orders.getName());
        Button deleteButton=(Button)convertView.findViewById(R.id.delete_btn);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabase.getReference("orders").child(orders.getName()).removeValue();
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
