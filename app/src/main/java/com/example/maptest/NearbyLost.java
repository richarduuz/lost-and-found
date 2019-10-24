package com.example.maptest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link listFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link listFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class NearbyLost extends Fragment {
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

    protected static ArrayList<PublishItem> items=new ArrayList<>();
    protected static ListView listView;
    protected static ListDemoAdapter mAdapter=null;
    private String provider;
    private LocationManager locationManager;


    /*
    private Button GotoPublish;
    private Button GotoFound;
    rearrange these buttons;
     */

    protected static String Found;
    protected static String currentLcation = MainActivity.targetBuilding;


    //    private Handler locationHandler = new Handler(){
//        public void
//    };
    public NearbyLost() {
        // Required empty public constructor
        float minDistance = 100000000;
        for (String b: MainActivity.buildings.keySet()){
            float dis = getDistance(MainActivity.myLocation, MainActivity.buildings.get(b));
            if (minDistance > dis){
                minDistance = dis;
                currentLcation = b;
            }
        }
        System.out.println("my location  is "+currentLcation);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView =  view.findViewById(R.id.item_list);
        Found = "False";
        fetchData();
        setHasOptionsMenu(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                PublishItem item = (PublishItem) parent.getItemAtPosition(position);
                String userId = item.getPublisher();
                String itemname = item.getItemName();
                String itemdescription = item.getItemDiscription();
                String Contact = item.getContact();
                String Phone = item.getPhone();
                String image = checkImageSize(item.getItemImage());
                Intent intent=new Intent(NearbyLost.this.getActivity(), ItemDetail.class);
                intent.putExtra("uid", userId);
                intent.putExtra("image", image);
                intent.putExtra("name", itemname);
                intent.putExtra("description", itemdescription);
                intent.putExtra("contactName", Contact);
                intent.putExtra("Phone", Phone);
                startActivity(intent);
            }
        });





        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                String curLocation = "";
                float minDis = 10000000;
                for (String b: MainActivity.buildings.keySet()){
                    float dis = getDistance(location, MainActivity.buildings.get(b));
                    if (minDis > dis){
                        minDis = dis;
                        curLocation = b;
                    }
                }
                currentLcation= curLocation;
                fetchData();


            }
        };
        return view;
    }


    public void fetchData(){
        NearbyLost.items.clear();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(currentLcation)
                .whereEqualTo("Found", Found)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (NearbyLost.this.getActivity()!=null&&task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()){
                                String Image = "null";
                                if(document.contains("Image")) {
                                    Image = (String)document.get("Image");
                                }//TODO process the image
                                String Name=(String)document.get("Name");
                                String Description=(String)document.get("Description");
                                String Uid=(String)document.get("publisher");
                                String Contact = (String)document.get("Contact");
                                String Phone = (String)document.get("Phone");
                                items.add(new PublishItem(Image,Name,Description,Uid,"False", Contact, Phone));
                            }
                            mAdapter=new ListDemoAdapter(NearbyLost.this.getActivity(), R.layout.list_layout, items);
                            listView.setAdapter(mAdapter);
                        }
                    }
                });
    }

    public static float getDistance(android.location.Location user, LatLng buidling){
        float[] result = new float[1];
        Location.distanceBetween(user.getLatitude(), user.getLongitude(), buidling.latitude, buidling.longitude, result);
        return result[0];
    }

    public static float getDistance(LatLng user, LatLng buidling){
        float[] result = new float[1];
        Location.distanceBetween(user.latitude, user.longitude, buidling.latitude, buidling.longitude, result);
        return result[0];
    }
    public String checkImageSize(String image){
        if(image == null){
            return "null";
        }
        byte[] data = android.util.Base64.decode(image, android.util.Base64.DEFAULT);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Read BitMap by file path.
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            int size = bitmap.getAllocationByteCount();
            int current_quality = 100;
            if (size > 700000){
                double resize = 700000.00/size * 100;
                current_quality = (int)resize;
                Log.d("Image", String.valueOf(resize));
            }
            Log.d("Image", String.valueOf(current_quality));

            bitmap.compress(Bitmap.CompressFormat.JPEG, current_quality, stream);
        } catch (Exception e) {
            Log.d("Image", "Image path error");
        }

        byte[] byteArray = stream.toByteArray();
        return java.util.Base64.getEncoder().encodeToString(byteArray);
    }

}
