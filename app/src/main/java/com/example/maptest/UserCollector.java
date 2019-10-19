package com.example.maptest;

public class UserCollector {

    private String userId;
    private String userName;

    public UserCollector(String id){
        this.userId = id;
        this.userName = "richarduzi" + id;
    }

    public UserCollector(String id, String name){
        this.userId = id;
        this.userName = name;

    }

    public String getUserId(){
        return this.userId;
    }

    public void setUserId(String id){
        this.userId = id;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String name){
        this.userName = name;
    }

}
