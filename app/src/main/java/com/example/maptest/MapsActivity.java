package com.example.maptest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Button button;
    private LocationManager locationManager;
    private String provider;
    private LatLng myLocation;
    private final float STARTZOOM=16;
    public static HashMap<String, LatLng> buildings= new HashMap<>();
    public static ArrayList<String> building2Marker= new ArrayList<>();

    private SearchView searchView;
    private ListView searchListView;
    private final float SEARCHZOOM = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, Register.class);
                startActivity(intent);
            }
        });
        searchView = findViewById(R.id.searchview);

        //show the submission button
        searchView.setSubmitButtonEnabled(true);
        //show hint to enter query
        searchView.setQueryHint("please enter ...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchListView = findViewById(R.id.searchlistview);

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.search_list, building2Marker);

        searchListView.setAdapter(adapter);
        searchListView.setTextFilterEnabled(false);
        Filter filter = adapter.getFilter();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try{
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(SEARCHZOOM));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(buildings.get(query)));
                    filter.filter("?");
                    searchView.clearFocus();
                }
                catch (Exception e){
                    searchView.clearFocus();
                    Toast.makeText(MapsActivity.this,"No Result", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    filter.filter("");
                }else{
                    filter.filter(newText);
                }
                return true;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Search", searchListView.getAdapter().getItem(i).toString());
                searchListView.clearTextFilter();
                mMap.moveCamera(CameraUpdateFactory.zoomTo(SEARCHZOOM));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(buildings.get(searchListView.getAdapter().getItem(i).toString())));
                filter.filter("?");
                searchView.clearFocus();
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                                mMap.addMarker(new MarkerOptions().position(buildings.get(building)).title(building));
                                building2Marker.add(building);
                            }
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(STARTZOOM));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(buildings.get("Eastern Resource Center")));//TODO change it to personal location later
                        }
                    }
                });

        mMap.setOnMarkerClickListener(this);


//        LatLng ERC=new LatLng(-37.799388, 144.962828);
//            LatLng AL=new LatLng(-37.798636, 144.963404);
//            LatLng DMD=new LatLng(-37.799073, 144.963104);
//            LatLng AEF=new LatLng(-37.798857, 144.963954);
//            LatLng PHB=new LatLng(-37.798042, 144.963703);
//            LatLng Stop1=new LatLng(-37.799964, 144.963775);
//            LatLng B1888=new LatLng(-37.799804, 144.963221);
//            LatLng DoME= new LatLng(-37.799932, 144.962271);
//
//            // Add a marker in Sydney and move the camera
//            mMap.addMarker(new MarkerOptions().position(ERC).title("Eastern Resource Center"));
//            mMap.addMarker(new MarkerOptions().position(AL).title("Alice Hoy"));
//            mMap.addMarker(new MarkerOptions().position(DMD).title("Doug McDonell"));
//            mMap.addMarker(new MarkerOptions().position(AEF).title("Asia Education Foundation"));
//            mMap.addMarker(new MarkerOptions().position(PHB).title("Peter Hall Building"));
//            //mMap.addCircle(new CircleOptions().center(ERC));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(ERC));
//            mMap.moveCamera(CameraUpdateFactory.zoomTo(STARTZOOM));
//            mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        String markerId=marker.getId();
        marker.showInfoWindow();
        Intent intent = new Intent(MapsActivity.this, BuildingActivity.class);
        switch(markerId){
            //TODO Jump to different activities respectively
            case("m0"):
                intent.putExtra("Location", building2Marker.get(0));
                startActivity(intent);
                break;
            case("m1"):
                intent.putExtra("Location", building2Marker.get(1));
                startActivity(intent);
                break;
            case("m2"):
                intent.putExtra("Location", building2Marker.get(2));
                startActivity(intent);
                break;
//            case("m3"):
//                intent.putExtra("Location", "AEF");
//                startActivity(intent);
//                break;
//            case("m4"):
//                intent.putExtra("Location", "PHB");
//                startActivity(intent);
//                break;
            default:
                return false;
        }
        return true;
    }
}
