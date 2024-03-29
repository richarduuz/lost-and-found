package com.example.maptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    private mapFragment map_fragment;
    private NearbyFragment nearby_fragment;
    private MeFragment meFragment;
    private chatListFragment chat_list_fragment;
    public static String targetBuilding = "Doug McDonell";
//    private MessageFragment messageFragment;
//    private MeFragment meFragment;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentAdapter fragmentAdpater;
    private BottomNavigationView btmMenu;
    private String provider;
    private LocationManager locationManager;
    private String[] FragmentTitles = {"Map", "List", "Message", "Me"};
    private GoogleMap map;

    private boolean buildingsGot = false;
    private static final int GPS_PERMISSION_CODE = 1;
    private static final int INTERNET_PERMISSION_CODE = 2;

    public static HashMap<String, LatLng> buildings= new HashMap<>();
    public static ArrayList<String> building2Marker= new ArrayList<>();
    public static ArrayList<Integer> buildingMarkerClick=new ArrayList<>();
    public static LatLng myLocation=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.INTERNET},
                    INTERNET_PERMISSION_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION},
                    GPS_PERMISSION_CODE);
            return;
        }
        grabBuildingsData();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener btmNavigationSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            // when click the menu, switch to specific fragment
            switch (menuItem.getItemId()){
                case R.id.navigation_map:{
                    viewPager.setCurrentItem(0);
                    return true;
                }
                case R.id.navigation_nearby:{
                    viewPager.setCurrentItem(1);
                    return true;
                }
                case R.id.navigation_message:{

                    viewPager.setCurrentItem(2);
                    return true;
                }
                case R.id.navigation_me:{
                    viewPager.setCurrentItem(3);
                    return true;
                }
            }
            return false;
        }
    };

    public void grabBuildingsData(){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("test");




        db.collection("test")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@Nonnull Task<QuerySnapshot> task){
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                buildings.put((String)document.get("Name"),new LatLng((double) document.get("Latitude"), (double) document.get("Longitude")));
                            }
                            addBuildingMarker();
                            myLocationInit();
                            fragmentInit();

                            fragmentAdpater = new FragmentAdapter(MainActivity.this.getSupportFragmentManager(), fragmentList);
                            viewPager.setAdapter(fragmentAdpater);

                            viewPager.setOffscreenPageLimit(4);

                            viewPager.setCurrentItem(0);
                            btmMenu = findViewById(R.id.bottom_menu);
                            btmMenu.setOnNavigationItemSelectedListener(btmNavigationSelectedListener);

                            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        }
                    }

                });

    }

    public void addBuildingMarker() {
        for (String building : buildings.keySet()) {
            //mMap.addMarker(new MarkerOptions().position(buildings.get(building)).title(building));
            building2Marker.add(building);
            buildingMarkerClick.add(0);
        }
    }

    public void myLocationInit(){
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        List<String> list=locationManager.getProviders(true);
        if(list.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }
        else if(list.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        }
        try {
            Location location=locationManager.getLastKnownLocation(provider);
            myLocation=new LatLng(location.getLatitude(),location.getLongitude());
        }catch(SecurityException e){
            Toast.makeText(this,"please allow the GPS permission",Toast.LENGTH_LONG).show();
        }catch (NullPointerException e){
            Toast.makeText(this,"please allow the GPS permission", Toast.LENGTH_LONG).show();
        }catch (IllegalArgumentException e){}
    }

    public void fragmentInit(){
        viewPager = findViewById(R.id.mainViewPager);
        map_fragment = new mapFragment();
        meFragment = new MeFragment();
        nearby_fragment = new NearbyFragment();
        chat_list_fragment = new chatListFragment();
        fragmentList.add(map_fragment);
        fragmentList.add(nearby_fragment);
        fragmentList.add(chat_list_fragment);
        fragmentList.add(meFragment);
    }

    public class FragmentAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> list = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> List){
            super(fm);
            this.list = List;

        }

        public Fragment getItem(int number){
            return fragmentList.get(number);
        }

        public int getCount(){
            return list.size();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == GPS_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
            else {
                Toast.makeText(MainActivity.this,
                        "GPS Permission Denied. Please Make sure GPS Permission is Grant. Otherwise, the App does not Work",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == INTERNET_PERMISSION_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Internet Permission Denied. Please Make sure Internet Permission is Grant. Otherwise, the App does not Work",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
