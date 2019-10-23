package com.example.maptest;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;


public class BuildingActivity extends AppCompatActivity {
    private ArrayList<PublishItem> items=new ArrayList<>();
    private ListView listView;
    private ListDemoAdapter mAdapter=null;
    private String Location;
    private String Found;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent intent=getIntent();
        Location=intent.getStringExtra("Location");
        Found=intent.getStringExtra("Found");
        fetchData();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void fetchData(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(Location)
                .whereEqualTo("Found", Found)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()){
                                String Image = "null";
                                if(document.contains("Image")) {
                                    Image = (String)document.get("Image");
                                }//TODO process the image
                                String Name=(String)document.get("Name");
                                String Description=(String)document.get("Description");
                                String Phone = (String)document.get("Phone");
                                String Contact = (String)document.get("Contact");
                                String userId;
                                if (document.contains("publisher")){userId=(String)document.get("publisher");}
                                else{userId="unknown";}
                                items.add(new PublishItem(Image,Name,Description,"False",userId, Contact, Phone));
                            }
                            listView= (ListView) findViewById(R.id.demo_list_view);
                            mAdapter=new ListDemoAdapter(BuildingActivity.this, R.layout.list_layout, items);
                            listView.setAdapter(mAdapter);
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
                                    String image = item.getItemImage();
                                    Intent intent=new Intent(BuildingActivity.this, ItemDetail.class);
                                    intent.putExtra("uid", userId);
                                    intent.putExtra("image", image);
                                    intent.putExtra("name", itemname);
                                    intent.putExtra("description", itemdescription);
                                    intent.putExtra("contactName", Contact);
                                    intent.putExtra("Phone", Phone);
                                    startActivity(intent);
                                    BuildingActivity.this.finish();
                                }
                            });
                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.publish_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==android.R.id.home) {
            this.finish();
            return true;
        }
        if(id == R.id.menu_publish){
            //What you want(Code Here)
            Intent intent=new Intent(BuildingActivity.this, GoToPublishItems.class);
            intent.putExtra("Location", Location);
            intent.putExtra("Found",Found);
            startActivity(intent);
            BuildingActivity.this.finish();
            return true;
        }
        if(id == R.id.menu_gotofoundlist){
            //What you want(Code Here)
            Intent intent = new Intent(BuildingActivity.this, BuildingActivity.class);
            intent.putExtra("Location", Location);
            if (Found.equals("False")){
                intent.putExtra("Found", "True");
                item.setTitle("Go to found list");
            }
            else{
                intent.putExtra("Found", "False");
                item.setTitle("Go to lost list");
            }
            startActivity(intent);
            BuildingActivity.this.finish();
            fetchData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
