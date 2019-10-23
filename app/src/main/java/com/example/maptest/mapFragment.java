package com.example.maptest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link mapFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link mapFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class mapFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
    private String provider;
    protected static LatLng myLocation;
    public static String targetBuilding = "Alice Hoy";
    private GoogleMap map;
    private LocationManager locationManager;
    private final float STARTZOOM=16;
    public static HashMap<String, LatLng> buildings= new HashMap<>();
    public static ArrayList<String> building2Marker= new ArrayList<>();
    public static ArrayList<Integer> buildingMarkerClick=new ArrayList<>();
    private static final int REQUEST_CODE = 1;
    private static final int GPS_PERMISSION_CODE = 1;
    private final float SEARCHZOOM = 19;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.searchTextView)
    TextView goToSearch;

    public mapFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
            mapInit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION},
                            GPS_PERMISSION_CODE);
                    return;
                }
                map.setMyLocationEnabled(true);
                building2Marker.clear();
                FirebaseFirestore db= FirebaseFirestore.getInstance();
                CollectionReference collection=db.collection("test");
                db.collection("test")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@Nonnull Task<QuerySnapshot> task){
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        buildings.put((String)document.get("Name"),new LatLng((double) document.get("Latitude"), (double) document.get("Longitude")));
                                    }
                                    for (String building : buildings.keySet()) {
                                        //mMap.addMarker(new MarkerOptions().position(buildings.get(building)).title(building));
                                        int height = 150;
                                        int width = 150;
                                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker5);
                                        Bitmap b=bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                        map.addMarker(new MarkerOptions().position(buildings.get(building)).title(building).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                        building2Marker.add(building);
                                        buildingMarkerClick.add(0);
                                    }
                                }
                            }
                        });
                map.moveCamera(CameraUpdateFactory.zoomTo(STARTZOOM));
                map.moveCamera(CameraUpdateFactory.newLatLng(myLocation));





                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String markerId = marker.getId();
                        markerId = markerId.substring(1, 2);
                        marker.showInfoWindow();
                        int markerClick=buildingMarkerClick.get(Integer.parseInt(markerId));
                        buildingMarkerClick.set(Integer.parseInt(markerId),markerClick+1);
                        if(buildingMarkerClick.get(Integer.parseInt(markerId))==2){
                            for(int i=0; i<buildingMarkerClick.size(); i++){
                                buildingMarkerClick.set(i,0);
                            }
                            Intent intent = new Intent(getActivity(), BuildingActivity.class);
                            intent.putExtra("Location", building2Marker.get(Integer.parseInt(markerId)));
                            intent.putExtra("Found", "False");
                            startActivity(intent);
                        }
                        return true;
                    }
                });


            }

        });


        locationManager=(LocationManager)mapFragment.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            Toast.makeText(getActivity(),"please allow the GPS permission",Toast.LENGTH_LONG).show();
        }catch (NullPointerException e){
            Toast.makeText(getActivity(),"please allow the GPS permission", Toast.LENGTH_LONG).show();
        }catch (IllegalArgumentException e){

        }

        // -------------- implementation of searching function
        goToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("search", "button click");
                Intent intent = new Intent(mapFragment.this.getActivity(), SearchActivity.class);
                intent.putExtra("building", building2Marker);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

//        closestBuilding c = new closestBuilding(myLocation, buildings, locationManager);
//        new Thread(c).start();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Log.d("query", data.getStringExtra("Query"));
            map.moveCamera(CameraUpdateFactory.zoomTo(SEARCHZOOM));
            map.moveCamera(CameraUpdateFactory.newLatLng(buildings.get(data.getStringExtra("Query"))));
        }
    }

    public void mapInit(){
        if (!Places.isInitialized()) {
            Places.initialize(mapFragment.this.getActivity().getApplicationContext(), "AIzaSyBtbw67hiHLGAhYsXmuXZmcPtO4hc2ehdU");
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

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
                Intent i = new Intent(mapFragment.this.getActivity(), MainActivity.class);
                startActivity(i);
            }
            else {
                Toast.makeText(mapFragment.this.getActivity(),
                        "GPS Permission Denied. Please Make sure GPS Permission is Grant. Otherwise, the App does not Work",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
