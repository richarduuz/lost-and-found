package com.example.maptest;

public class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color){
        this.name = name;
        this.color = color;
    }

    public MemberData(){}

    public String getName(){
        return this.name;
    }

    public String getColor(){
        return this.color;
    }

}
