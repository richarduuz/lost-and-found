package com.example.maptest;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class closestBuilding implements Runnable {
    private LatLng curLocation;
    private HashMap<String, LatLng> map;
    private float minDis;
    private String target = "Alice Hoy";
    private LocationManager locationManager;
    private String provider="";


    public closestBuilding(LatLng location, HashMap<String, LatLng> buildings, LocationManager locationManager){
        this.curLocation = location;
        this.map = buildings;
        this.locationManager = locationManager;
        this.minDis = 1000000000;
    }

    public void run(){
        while(true){
            this.minDis = 10000000;

            List<String> list=locationManager.getProviders(true);
            if(list.contains(LocationManager.GPS_PROVIDER)){
                provider=LocationManager.GPS_PROVIDER;
            }
            else if(list.contains(LocationManager.NETWORK_PROVIDER)){
                provider=LocationManager.NETWORK_PROVIDER;
            }
            try {
                Location location=locationManager.getLastKnownLocation(provider);
                curLocation=new com.google.android.gms.maps.model.LatLng(location.getLatitude(),location.getLongitude());
            }catch(SecurityException e){

            }catch (NullPointerException e){

            }

            for(String key:map.keySet()){
                float dis = getDistance(curLocation, map.get(key));
                if (dis < minDis){
                    minDis = dis;
                    target = key;
                }
            }
            MainActivity.targetBuilding = target;
            System.out.println("The closest building is: " + target);
            try {
                Thread.sleep(300000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public static float getDistance(LatLng user, LatLng buidling){
        float[] result = new float[1];
        Location.distanceBetween(user.latitude, user.longitude, buidling.latitude, buidling.longitude, result);
        return result[0];
    }
}
