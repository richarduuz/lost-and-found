package com.example.maptest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class GoToPublishItems extends AppCompatActivity {
    private String Location;
    private String Found;
    private EditText Name;
    private EditText Description;
    private byte[] Image;
    private Button publish;
    private Button selectIamge;
    private ImageView upload_imageview;
    public static final int GALLERY_REQUEST_CODE = 1;
    private Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gotopublishitems);
        Intent intent=getIntent();
        Location=intent.getStringExtra("Location");
        Found=intent.getStringExtra("Found");
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
                        intent1.putExtra("Found",Found);
                        startActivity(intent1);
                        GoToPublishItems.this.finish();
                    }
                };
                timer.schedule(task1,1000);
            }
        });
        selectIamge=findViewById(R.id.uploadimage);
        upload_imageview = findViewById(R.id.upload_imageview);
        selectIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonClick", "button click");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void upload(String name, String description, String Location){
        Map<String, String> lostThing=new HashMap<>();
        lostThing.put("Name",name);
        lostThing.put("Description",description);
        lostThing.put("Found",Found);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            uploadUri = data.getData();
            upload_imageview.setImageURI(uploadUri);
        }
        else {
            Log.e("Gallery", "Gallery upload error");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

