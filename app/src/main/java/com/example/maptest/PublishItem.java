package com.example.maptest;

import android.content.Context;

public class PublishItem {
    private String itemImage;
    private String itemName;
    private String itemDiscription=null;
    private String Found;
    private String publisher;
    private String Phone;
    private String Contact;

    PublishItem(String image, String name, String discription, String Found, String publisher){
        itemImage=image;
        itemName=name;
        itemDiscription=discription;
        this.Found=Found;
        this.publisher=publisher;
    }

    PublishItem(String image, String name, String discription, String Found, String publisher, String contact, String phone){
        itemImage=image;
        itemName=name;
        itemDiscription=discription;
        this.Found=Found;
        this.publisher=publisher;
        this.Phone = phone;
        this.Contact = contact;
    }

    PublishItem(String image, String name){
        itemImage=image;
        itemName=name;
        itemDiscription=null;
    }

    public String getItemImage(){
        return itemImage;
    }

    public String getItemName(){
        return this.itemName;
    }

    public String getItemDiscription(){
        return itemDiscription;
    }

    public String getPublisher() {return publisher;}

    public String getPhone() {return this.Phone;}

    public String getContact() {return Contact;}

}
