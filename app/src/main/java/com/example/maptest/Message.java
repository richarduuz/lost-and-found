package com.example.maptest;


import java.sql.Timestamp;
import java.util.Comparator;

public class Message {
    private String Content;
    private String Sender;
    private long TimeStamp;
    private boolean BelongToSender;
    private String senderName;

    public Message(){}

    public Message(String content, String sender, long timestamp){
        this.Content=content;
        this.Sender=sender;
        this.TimeStamp=timestamp;
    }

    public Message(String content, String sender, long timestamp, boolean input){
        this.Content=content;
        this.Sender=sender;
        this.TimeStamp=timestamp;
        this.BelongToSender=input;
    }

    public Message(String content, String sender, long timestamp,boolean input, String senderName){
        this.Content=content;
        this.Sender=sender;
        this.TimeStamp=timestamp;
        this.BelongToSender=input;
        this.senderName=senderName;
    }

    public Message(String content, String sender, long timeStamp, String senderName){
        this.Content=content;
        this.Sender=sender;
        this.TimeStamp=timeStamp;
        this.senderName=senderName;
    }

    public long getTimeStamp(){
        return this.TimeStamp;
    }

    public String getSender(){
        return this.Sender;
    }

    public String getContent(){
        return this.Content;
    }

    public boolean isBelongToSender(){return this.BelongToSender;}

    public String getSenderName() {return this.senderName;}

}
