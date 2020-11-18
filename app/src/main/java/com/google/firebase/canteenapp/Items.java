package com.google.firebase.canteenapp;

public class Items {

    private String name;
    private String price;

    public Items(){

    }
    public Items(String text,String Price){

        this.name=text;
        this.price=Price;
    }

    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
}
