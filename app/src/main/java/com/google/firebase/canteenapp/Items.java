package com.google.firebase.canteenapp;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Items {

    private String name;
    private String price;
    private int mQuantity;

    public Items() {

    }

    public Items(String text, String Price) {

        this.name = text;
        this.price = Price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public void addToQuantity() {
        this.mQuantity += 1;
    }

    public void removeFromQuantity() {
        if (this.mQuantity > 0) {
            this.mQuantity -= 1;
        }
    }
}