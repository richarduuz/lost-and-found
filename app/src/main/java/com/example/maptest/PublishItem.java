package com.example.maptest;

import android.content.Context;

public class PublishItem {
    private int itemImage;
    private String itemName;
    private String itemDiscription=null;
    private String Found;
    private String publisher;
    private String Phone;
    private String Contact;

    PublishItem(int image, String name, String discription, String Found, String publisher){
        itemImage=image;
        itemName=name;
        itemDiscription=discription;
        this.Found=Found;
        this.publisher=publisher;
    }

    PublishItem(int image, String name, String discription, String Found, String publisher, String contact, String phone){
        itemImage=image;
        itemName=name;
        itemDiscription=discription;
        this.Found=Found;
        this.publisher=publisher;
        this.Phone = phone;
        this.Contact = contact;
    }

    PublishItem(int image, String name){
        itemImage=image;
        itemName=name;
        itemDiscription=null;
    }

    public int getItemImage(){
        return itemImage;
    }

    public String getItemName(){
        return this.itemName;
    }

    public String getItemDiscription(){
        return itemDiscription;
    }

    public String getPublisher() {return publisher;}

    public String getPhone() {return publisher;}

    public String getContact() {return Contact;}

}
