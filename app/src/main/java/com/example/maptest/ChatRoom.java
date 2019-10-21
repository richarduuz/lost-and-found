package com.example.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ChatRoom extends AppCompatActivity{
    private String TAG="TEST";
    private MessageAdapter mAdapter = null;
    private ArrayList<Message> messages= new ArrayList<>();
    private String thisSender="HeoJb4NSYOO1rqaTQJ2F8kggzEy1";
    private ListView listView;
    private ImageButton sendMessage;
    private EditText messageBox;
    private int lastMessage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        Intent intent=getIntent();
        String uid = intent.getStringExtra("uid");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HeoJb4NSYOO1rqaTQJ2F8kggzEy1").child(uid);
        sendMessage=findViewById(R.id.sendMessage);
        messageBox=findViewById(R.id.messageBox);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String sender=thisSender;
                    String content=messageBox.getText().toString().trim();
                    long currentTime=System.currentTimeMillis();
                    Message message = new Message(content, sender,currentTime);
                    Map<String, Object> sendMyMessage=new HashMap<>();
                    String key=lastMessage+1+"";
                    key="Message"+key;
                    sendMyMessage.put(key,message);
                    myRef.updateChildren(sendMyMessage);
                    messageBox.setText("");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                lastMessage=0;
                messages.clear();
                for(DataSnapshot messageSnapshot:dataSnapshot.getChildren()){
                    lastMessage++;
                    String content=(String)messageSnapshot.child("content").getValue();
                    String sender=(String)messageSnapshot.child("sender").getValue();
                    long myTimeStamp=(long)messageSnapshot.child("timeStamp").getValue();
                    Message message;
                    if(sender.equals(thisSender)) message=new Message(content, sender, myTimeStamp, TRUE);
                    else message=new Message(content, sender, myTimeStamp, FALSE);
                    messages.add(message);
                }
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message message1, Message message2) {
                        return (message1.getTimeStamp()<message2.getTimeStamp()?0:1);
                    }
                });
                mAdapter=new MessageAdapter(ChatRoom.this, messages);
                listView=(ListView)findViewById(R.id.messages_view);
                listView.setAdapter(mAdapter);
                Log.d(TAG, "Value is: ");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
