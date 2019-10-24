package com.example.maptest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MeFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MeFragment extends Fragment {

    private FirebaseUser user;
    public static final int CAMERA_REQUEST_CODE = 0;
    public static final int GALLERY_REQUEST_CODE = 1;
    public static final int GALLERY_PERMISSION_CODE = 1;
    public static final int CAMERA_PERMISSION_CODE = 2;
    private Uri photoUri;
    private MainActivity.FragmentAdapter fragmentAdpater;
    private ViewPager viewPager;

    @BindView(R.id.photo_imageview)
    ImageView photo;
    @BindView(R.id.username_profile)
    TextView username;
    @BindView(R.id.email_address_profile)
    TextView email_address;
    @BindView(R.id.sign_out)
    Button signoutbtn;
    @BindView(R.id.profile)
    TextView profile;
    @BindView(R.id.change_password)
    Button resetpwd;
    @BindView(R.id.icon_mood)
    ImageView ic_mood;
    @BindView(R.id.icon_email)
    ImageView ic_email;


    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
//            retrieve user profile from Firebase
            String name = user.getDisplayName();
            username.setText(name);
            String email = user.getEmail();
            email_address.setText(email);
            Uri photoUrl = user.getPhotoUrl();
            Picasso.with(getActivity()).load(photoUrl).into(photo);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("me", "photo");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Please choose");
                    final String[] chioces = {"Take a photo from camera", "Upload a photo from Gallery"};
                    builder.setItems(chioces, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i){
                                case 0:
                                    if (checkSelfPermission(MeFragment.this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[] { Manifest.permission.CAMERA},
                                                CAMERA_PERMISSION_CODE);
                                        return;
                                    }
                                    File image = new File(getActivity().getExternalCacheDir(), "photo.jpg");
                                    try {
                                        if (image.exists()) {
                                            image.delete();
                                            image.createNewFile();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", image);
                                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                    startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
                                    break;
                                case 1:
                                    if (checkSelfPermission(MeFragment.this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                            && checkSelfPermission(MeFragment.this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                GALLERY_PERMISSION_CODE);
                                        return;
                                    }
                                    Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(gallery_intent, GALLERY_REQUEST_CODE);
                                    break;
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            signoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent sign_out_intent = new Intent(MeFragment.this.getActivity(), MainActivity.class);
                    startActivity(sign_out_intent);
                    MeFragment.this.getActivity().finish();
                }
            });

            resetpwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Please reter your new password");
                    final EditText et = new EditText(MeFragment.this.getActivity());
                    builder.setView(et);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String new_pwd = et.getText().toString().trim();
                            if (TextUtils.isEmpty(new_pwd)){
                                Toast.makeText(MeFragment.this.getActivity(), "The new password cannot be null", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                user.updatePassword(new_pwd)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(MeFragment.this.getActivity(), "User password updated.", Toast.LENGTH_SHORT).show();
                                                    Log.d("MeFragment", "User password updated.");
                                                }
                                                else{
//                                                    Toast.makeText(MeFragment.this.getActivity(), "update error ", Toast.LENGTH_SHORT).show();
                                                    Log.d("MeFragment", "update error");
                                                }
                                            }
                                        });
                                FirebaseAuth.getInstance().signOut();
                                Intent sign_out_intent = new Intent(MeFragment.this.getActivity(), MainActivity.class);
                                startActivity(sign_out_intent);
                                MeFragment.this.getActivity().finish();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
        else{
            photo.setImageResource(R.drawable.noimage);
            resetpwd.setVisibility(View.INVISIBLE);
            username.setText("Please Login first");
            ic_mood.setVisibility(View.INVISIBLE);
            ic_email.setVisibility(View.INVISIBLE);
            profile.setText("");
            signoutbtn.setText("Log in");
            signoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login_intent = new Intent(MeFragment.this.getActivity(), Login.class);
                    startActivity(login_intent);
                    MeFragment.this.getActivity().finish();
                }
            });
        }
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            photoUri = data.getData();
            photo.setImageURI(photoUri);
            updatePhoto(photoUri);
        }
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            photo.setImageURI(photoUri);
            updatePhoto(photoUri);
        }
        else {
            Log.e("Gallery", "Gallery upload error");
        }
    }


    private void updatePhoto(Uri uri){

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "User profile updated.");
                        }
                    }
                });
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
                Log.d("MeFragment", "Gallery Permission Granted.");
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, GALLERY_REQUEST_CODE);
            }
            else {
                Toast.makeText(MeFragment.this.getActivity(),
                        "Gallery Permission Denied. You cannot select images from Gallery",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File image = new File(getActivity().getExternalCacheDir(), "photo.jpg");
                try {
                    if (image.exists()) {
                        image.delete();
                        image.createNewFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", image);
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
            }
            else {
                Toast.makeText(MeFragment.this.getActivity(),
                        "Camera Permission Denied. You cannot select images from Gallery",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
