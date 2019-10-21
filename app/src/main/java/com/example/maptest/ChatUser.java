package com.example.maptest;

public class ChatUser {
    private String uid;
    private String userName;
    private int userIcon;

    public ChatUser(String uid, String username){
        this.uid=uid;
        this.userName=username;
    }

    public ChatUser(String uid, String username, int userIcon){
        this.uid=uid;
        this.userName=username;
        this.userIcon=userIcon;
    }

    public String getUid(){return this.uid;}
    public String getUserName(){return this.userName;}
    public int getUserIcon(){return this.userIcon;}

}
