package com.example.maptest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link listFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link listFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class listFragment extends Fragment {
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
    /*
    private Button GotoPublish;
    private Button GotoFound;
    rearrange these buttons;
     */

    protected static String Found;
    protected static String Location = MainActivity.targetBuilding;
    public listFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
//        GotoPublish = view.findViewById(R.id.publish);
//        GotoFound = view.findViewById(R.id.toFound);
        listView =  view.findViewById(R.id.item_list);
        Found = "False";
//        if (Found.equals("False")) GotoFound.setText("Go to found list");
//        else GotoFound.setText("Go to lost list");
        fetchData();
        updateClosestBuilding update = new updateClosestBuilding(listFragment.this);
        new Thread(update).start();
        setHasOptionsMenu(true);
//        GotoPublish.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent=new Intent(listFragment.this.getActivity(), GoToPublishItems.class);
//                intent.putExtra("Location", Location);
//                intent.putExtra("Found",Found);
//                startActivity(intent);
//            }
//        });
//
//        GotoFound.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Found.equals("False")){
//                    Found = "True";
//                }
//                else{
//                    Found = "False";
//                }
//                if (Found.equals("False")) GotoFound.setText("Go to found list");
//                else GotoFound.setText("Go to lost list");
//                items.clear();
//                fetchData();
//            }
//        });
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
                int image = item.getItemImage();
                Intent intent=new Intent(listFragment.this.getActivity(), ItemDetail.class);
                intent.putExtra("uid", userId);
                intent.putExtra("image", String.valueOf(image));
                intent.putExtra("name", itemname);
                intent.putExtra("description", itemdescription);
                intent.putExtra("contactName", Phone);
                intent.putExtra("Phone", Contact);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate your Menu
        inflater.inflate(R.menu.swich_menu, menu);

        // Get the action view used in your toggleservice item
        final MenuItem switch_item = menu.findItem(R.id.switch_item);
        final Switch actionView = (Switch) switch_item.getActionView();
        actionView.setShowText(true);
        actionView.setTextOn("Found");
        actionView.setTextOff("Lost");
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Start or stop your Service
                if (Found.equals("False")){
                    Found = "True";
                    switch_item.setTitle("Found");
                }
                else{
                    Found = "False";
                    switch_item.setTitle("Lost");
                }
                items.clear();
                fetchData();
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void fetchData(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(Location)
                .whereEqualTo("Found", Found)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (listFragment.this.getActivity()!=null&&task.isSuccessful()){
                            for (QueryDocumentSnapshot document:task.getResult()){
                                int Image=R.drawable.noimage;
                                if(document.contains("Image")) {}//TODO process the image
                                String Name=(String)document.get("Name");
                                String Description=(String)document.get("Description");
                                String Uid=(String)document.get("publisher");
                                String Contact = (String)document.get("Contact");
                                String Phone = (String)document.get("Phone");
                                items.add(new PublishItem(Image,Name,Description,Uid,"False", Contact, Phone));
                            }
                            mAdapter=new ListDemoAdapter(listFragment.this.getActivity(), R.layout.list_layout, items);
                            System.out.println("ready for content");
                            listView.setAdapter(mAdapter);
                        }
                    }
                });
    }
}
