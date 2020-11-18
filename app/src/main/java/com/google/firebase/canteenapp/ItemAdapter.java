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

import org.w3c.dom.Text;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Items> {

    private int count;
    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<Items> objects) {
        super(context, resource, objects);
        count=0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        }

        TextView nameTextView=(TextView)convertView.findViewById(R.id.name);
        TextView priceTextView=(TextView)convertView.findViewById(R.id.price);
        final TextView quantityTextView=(TextView)convertView.findViewById(R.id.quantity_text_view);

        Button incrementButton=(Button)convertView.findViewById(R.id.increment);
        Button decrementButton= (Button)convertView.findViewById(R.id.decrement);
        final Items items=getItem(position);

        nameTextView.setText(items.getName());
        priceTextView.setText(items.getPrice());


        // Added increment and decrement quantity button for each list item and also modified Items.java file accordingly
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.addToQuantity();
                quantityTextView.setText(Integer.toString(items.getmQuantity()));
                notifyDataSetChanged();
            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.removeFromQuantity();
                quantityTextView.setText(Integer.toString(items.getmQuantity()));
                notifyDataSetChanged();
            }
        });


        return convertView;
    }
}
