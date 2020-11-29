package com.google.firebase.canteenapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
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
                mFirebaseDatabase.getReference("orders").child(orders.getCanteenName()).child(orders.getName()).removeValue();
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

  /*  public void sendSMS() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=yogeshwarram.godara_comp2018%40pccoer.in&key=EC8AE951-FCCD-590E-CD61-60AF131CCD81&sms=%2B919834754408&message=This%20is%20testing&schedule=1377959755&senderid=MyCompany&return=http%3A%2F%2Fyourwebsite.com&method=mashape");
        Request request = new Request.Builder()
                .url("https://inteltech.p.rapidapi.com/send.php")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("x-rapidapi-key", "34904d8b5cmsh51ae48bd0011b89p114dd2jsn6c71b1070870")
                .addHeader("x-rapidapi-host", "inteltech.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
    }
    */

}
