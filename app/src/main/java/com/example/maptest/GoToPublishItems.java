package com.example.maptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class GoToPublishItems extends FragmentActivity {
    private String Location;
    private EditText Name;
    private EditText Description;
    private byte[] Image;
    private Button publish;
    private Button selectIamge;
    public static final int GALLERY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gotopublishitems);
        Intent intent=getIntent();
        Location=intent.getStringExtra("Location");
        Name=findViewById(R.id.item_name);
        Description=findViewById(R.id.item_description);
        publish=findViewById(R.id.gotopublish);
        publish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String name=Name.getText().toString().trim();
                String description=Description.getText().toString().trim();
                upload(name,description,Location);
                Toast.makeText(GoToPublishItems.this, "successfully added", Toast.LENGTH_LONG).show();
                Timer timer=new Timer();
                TimerTask task1=new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent1=new Intent(GoToPublishItems.this, BuildingActivity.class);
                        intent1.putExtra("Location",Location);
                        startActivity(intent1);
                        GoToPublishItems.this.finish();
                    }
                };
                timer.schedule(task1,1000);
            }
        });
        selectIamge=findViewById(R.id.uploadimage);
        selectIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_PICK);//Pick an item fromthe data
                intent.setType("image/*");//从所有图片中进行选择
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
            }
        });
    }

    public void upload(String name, String description, String Location){
        Map<String, String> lostThing=new HashMap<>();
        lostThing.put("Name",name);
        lostThing.put("Description",description);
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection(Location)
                .add(lostThing)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("lostThing added", "lost thing added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failure","ERROR");
                    }
                });
    }

    public void upload(String name, String description, byte[] image, String Location){

    }

    public void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode, data);
//        if (resultCode== Activity.RESULT_OK){
//            switch (requestCode){
//                case GALLERY_REQUEST_CODE:
//                    Uri selectedImage=data.getData();
//                    imageView.setImageURI
//            }
//        }
//    }
}

