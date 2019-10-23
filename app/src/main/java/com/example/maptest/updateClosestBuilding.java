package com.example.maptest;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class updateClosestBuilding implements Runnable {
    private listFragment list;
    private ArrayList<PublishItem> itemList;


    public updateClosestBuilding(listFragment l){
        this.list = l;
        this.itemList = new ArrayList<>();
    }

    public void run(){
        while(true){
            if (!listFragment.Location.equals(MainActivity.targetBuilding) && list.getActivity() != null){
                itemList.clear();
//                listFragment.items.clear();
                listFragment.Location = MainActivity.targetBuilding;
                System.out.println("Found update, clear the arrayList");
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                db.collection(listFragment.Location)
                        .whereEqualTo("Found", listFragment.Found)
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
                                        String Phone = (String)document.get("Phone");
                                        String Contact = (String)document.get("Contact");
                                        String Uid=(String)document.get("publisher");
                                        itemList.add(new PublishItem(Image,Name,Description,"False", Uid, Contact, Phone));
                                    }
                                    listFragment.mAdapter = new ListDemoAdapter(list.getActivity(), R.layout.list_layout, itemList);
//                                    for (PublishItem i:items){
//                                        System.out.println("Found an item");
//                                    }

                                    listFragment.listView.setAdapter(listFragment.mAdapter);
                                }
                            }
                        });
            }
        }

    }
}
