package com.google.firebase.canteenapp;

public class Orders {
    String name;
    String canteenName;
    public Orders(){

    }
    public Orders(String name,String canteenname){
        this.name=name;
        this.canteenName=canteenname;
    }
    public String getName(){
        return name;
    }
    public String getCanteenName(){
        return canteenName;
    }
}
