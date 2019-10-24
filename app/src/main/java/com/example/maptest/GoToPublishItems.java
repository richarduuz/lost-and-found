package com.example.maptest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class GoToPublishItems extends AppCompatActivity {
    private String Location;
    private String Found;
    /*
    add new members
     */
    private String image_string;
    private EditText Phone;
    private EditText ContactName;
//    private Button camerabtn;
    public static final int CAMERA_REQUEST_CODE = 0;
    /*
    add end
     */
    private EditText Name;
    private EditText Description;
    private byte[] Image;
    private Button publish;
//    private Button selectIamge;
    private ImageView upload_imageview;
    public static final int GALLERY_REQUEST_CODE = 1;
    public static final int GALLERY_PERMISSION_CODE = 1;
    public static final int CAMERA_PERMISSION_CODE = 2;
    private Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gotopublishitems);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=getIntent();
        Location=intent.getStringExtra("Location");
        Found=intent.getStringExtra("Found");
        Name=findViewById(R.id.item_name);
        Phone = findViewById(R.id.item_phone);
        ContactName = findViewById(R.id.item_contact);
        Description=findViewById(R.id.item_description);
        publish=findViewById(R.id.gotopublish);
        publish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(currentUser!=null){
                    String name=Name.getText().toString().trim();
                    String description=Description.getText().toString().trim();
                    String phone = Phone.getText().toString().trim();
                    String contact = ContactName.getText().toString().trim();

                    String currentUId = currentUser.getUid();
                    upload(name,description,Location,currentUId, image_string, phone, contact);
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoToPublishItems.this);
                    builder.setTitle("Please Login First!!!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent login_intent = new Intent(GoToPublishItems.this, Login.class);
                            startActivity(login_intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
//        selectIamge=findViewById(R.id.uploadimage);
        upload_imageview = findViewById(R.id.upload_imageview);
        upload_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GoToPublishItems.this);
                builder.setTitle("Please choose");
                final String[] chioces = {"Take a photo from camera", "Upload a photo from Gallery"};
                builder.setItems(chioces, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                if (ContextCompat.checkSelfPermission(GoToPublishItems.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(GoToPublishItems.this, new String[] { Manifest.permission.CAMERA},
                                            CAMERA_PERMISSION_CODE);
                                    return;
                                }
                                File image = new File(getExternalCacheDir(), "lostphoto.jpg");
                                try {
                                    if (image.exists()) {
                                        image.delete();
                                        image.createNewFile();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                uploadUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", image);
                                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri);
                                startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(GoToPublishItems.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(GoToPublishItems.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(GoToPublishItems.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            GALLERY_PERMISSION_CODE);
                                    return;
                                }
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, GALLERY_REQUEST_CODE);
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void upload(String name, String description, String Location, String currentUId, String image, String phone, String contact){
        Map<String, String> lostThing=new HashMap<>();
        lostThing.put("Name",name);
        lostThing.put("Description",description);
        lostThing.put("Found",Found);
        lostThing.put("publisher",currentUId);
        lostThing.put("Image", image);
        lostThing.put("Contact", contact);
        lostThing.put("Phone", phone);
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
            uriToString(uploadUri);
        }
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            upload_imageview.setImageURI(uploadUri);
            uriToString(uploadUri);
        }
        else {
            Log.e("Gallery", "Gallery upload error");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void uriToString(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Read BitMap by file path.
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            int size = bitmap.getAllocationByteCount();
            System.out.println("imageSize is:" + size);
            int current_quality = 100;
            if (size > 7000000){
                double resize = 7000000.00/size * 100;
                current_quality = (int)resize;
                Log.d("Image", String.valueOf(resize));
            }
            Log.d("Image", String.valueOf(current_quality));

            bitmap.compress(Bitmap.CompressFormat.JPEG, current_quality, stream);
        } catch (Exception e) {
            Log.d("Image", "Image path error");
        }

        byte[] byteArray = stream.toByteArray();
        image_string = Base64.getEncoder().encodeToString(byteArray);
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

        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
            else {
                Toast.makeText(GoToPublishItems.this,
                        "Gallery Permission Denied. You cannot select images from Gallery",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File image = new File(getExternalCacheDir(), "lostphoto.jpg");
                try {
                    if (image.exists()) {
                        image.delete();
                        image.createNewFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", image);
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri);
                startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
            }
            else {
                Toast.makeText(GoToPublishItems.this,
                        "Camera Permission Denied. You cannot select images from Gallery",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}

