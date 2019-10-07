package com.example.maptest;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private Button GotoPublish;
    private Button GotoFound;
    private String Location;
    private String Found;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent intent=getIntent();
        Location=intent.getStringExtra("Location");
        Found=intent.getStringExtra("Found");
        GotoPublish=findViewById(R.id.publishItem);
        GotoFound=findViewById(R.id.gotofound);
        if (Found.equals("False")) GotoFound.setText("Go to found list");
        else GotoFound.setText("Go to lost list");
        GotoPublish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(BuildingActivity.this, GoToPublishItems.class);
                intent.putExtra("Location", Location);
                intent.putExtra("Found",Found);
                startActivity(intent);
                BuildingActivity.this.finish();
            }
        });
        GotoFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuildingActivity.this, BuildingActivity.class);
                intent.putExtra("Location", Location);
                if (Found.equals("False")) intent.putExtra("Found", "True");
                else intent.putExtra("Found", "False");
                startActivity(intent);
                BuildingActivity.this.finish();
            }
        });
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(Location)
                .whereEqualTo("Found", Found)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()){
                                int Image=R.drawable.noimage;
                                if(document.contains("Image")) {}//TODO process the image
                                String Name=(String)document.get("Name");
                                String Description=(String)document.get("Description");
                                listView= (ListView) findViewById(R.id.demo_list_view);
                                items.add(new PublishItem(Image,Name,Description,"False"));
                                mAdapter=new ListDemoAdapter(BuildingActivity.this, R.layout.list_layout, items);
                                listView.setAdapter(mAdapter);
                            }

                        }
                    }
                });

//        listView= (ListView) findViewById(R.id.demo_list_view);
//        items.add(new PublishItem(R.drawable.noimage, Location));
//        mAdapter=new ListDemoAdapter(BuildingActivity.this, R.layout.list_layout, items);
//        listView.setAdapter(mAdapter);
    }
    //TODO get the item details from the databaes
}
