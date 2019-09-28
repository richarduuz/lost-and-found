package com.example.maptest;

public class PublishItem {
    private int itemImage;


    private String itemName;
    private String itemDiscription=null;

    PublishItem(int image, String name, String discription){
        itemImage=image;
        itemName=name;
        itemDiscription=discription;
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
