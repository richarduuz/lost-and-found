package com.example.maptest;

public class PublishItem {
    private int itemImage;


    private String itemName;
    private String itemDiscription=null;
    private String Found;

    PublishItem(int image, String name, String discription, String Found){
        itemImage=image;
        itemName=name;
        itemDiscription=discription;
        this.Found=Found;
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
}
