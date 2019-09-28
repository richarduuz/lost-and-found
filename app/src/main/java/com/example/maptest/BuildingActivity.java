package com.example.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class BuildingActivity extends AppCompatActivity {
    private ArrayList<PublishItem> publishItems;
    private ListView listView;
    private ListDemoAdapter mAdapter=null;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent intent=getIntent();
        String Location=intent.getStringExtra("Location");
        firebaseAuth= FirebaseAuth.getInstance();




        listView= (ListView) findViewById(R.id.demo_list_view);
        ArrayList<PublishItem> items=new ArrayList<>();
        items.add(new PublishItem(R.drawable.noimage, Location));
        mAdapter=new ListDemoAdapter(BuildingActivity.this, R.layout.list_layout, items);
        listView.setAdapter(mAdapter);
    }
    //TODO get the item details from the databaes
}
