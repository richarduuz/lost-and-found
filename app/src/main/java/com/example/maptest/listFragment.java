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




    protected ArrayList<PublishItem> items=new ArrayList<>();
    protected static ListView listView;
    protected static ListDemoAdapter mAdapter=null;
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
        listView =  view.findViewById(R.id.item_list);
        Found = "False";
        fetchData();
        updateClosestBuilding update = new updateClosestBuilding(listFragment.this);
        new Thread(update).start();
        setHasOptionsMenu(true);
        return view;
    }

    public void fetchData(){
        items.clear();
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
                                items.add(new PublishItem(Image,Name,Description,"False"));

                            }
                            mAdapter=new ListDemoAdapter(listFragment.this.getActivity(), R.layout.list_layout, items);
                            System.out.println("ready for content: " + Location);
                            listView.setAdapter(mAdapter);
                        }
                    }
                });
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
//        if (Found.equals("False"))
//            switch_item.setTitle("Go to found list");
//        else
//            switch_item.setTitle("Go to lost list");
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
//                if (Found.equals("False")) item.setTitle("Go to found list");
//                else item.setTitle("Go to lost list");
                items.clear();
                fetchData();
                }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.menu_publish){
//            //What you want(Code Here)
//            Intent intent=new Intent(listFragment.this.getActivity(), GoToPublishItems.class);
//            intent.putExtra("Location", Location);
//            intent.putExtra("Found",Found);
//            startActivity(intent);
//            return true;
//        }
//        if(id == R.id.menu_gotofoundlist){
//            //What you want(Code Here)
//            if (Found.equals("False")){
//                Found = "True";
//            }
//            else{
//                Found = "False";
//            }
//            if (Found.equals("False")) item.setTitle("Go to found list");
//            else item.setTitle("Go to lost list");
//            items.clear();
//            fetchData();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
