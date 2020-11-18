package com.google.firebase.canteenapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Items> {


    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<Items> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        }

        TextView nameTextView=(TextView)convertView.findViewById(R.id.name);
        TextView priceTextView=(TextView)convertView.findViewById(R.id.price);

        Items items=getItem(position);

        nameTextView.setText(items.getName());
        priceTextView.setText(items.getPrice());

        return convertView;
    }
}
