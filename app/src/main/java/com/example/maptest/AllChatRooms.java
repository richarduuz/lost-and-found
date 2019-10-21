package com.example.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllChatRooms extends AppCompatActivity {
    private String uid = "HeoJb4NSYOO1rqaTQJ2F8kggzEy1";
    private String TAG = "MAPTEST";
    private ArrayList<ChatUser> allUsers= new ArrayList<>();
    private AllChatRoomsAdapter mAdapter = null;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_chatrooms);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsers.clear();
                for(DataSnapshot chatWithuser: dataSnapshot.getChildren()){
                    String chatUid=(String)chatWithuser.getKey();
                    String chatUsername="chang Liu";
                    int userIcon=R.drawable.noimage;
                    allUsers.add(new ChatUser(chatUid,chatUsername,userIcon));
                }
                listView=(ListView) findViewById(R.id.all_chatrooms);
                mAdapter = new AllChatRoomsAdapter(AllChatRooms.this, R.layout.list_layout, allUsers);
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ChatUser user=(ChatUser) parent.getItemAtPosition(position);
                        String userId = user.getUid();
                        Intent intent = new Intent(AllChatRooms.this, ChatRoom.class);
                        intent.putExtra("uid", userId);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

    }

}
