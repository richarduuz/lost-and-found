package com.example.maptest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private final String ALLSESSIONS="AllSessions";
    private String currentUid=null;
    private String currentUserName = null;
    private String uid=null;
    private ListView listView;
    private ImageButton sendMessage;
    private EditText messageBox;
    private long lastMessage=0;
    private FirebaseDatabase database;
    private String sessionKey=null;
    private String contact=null;

    public static final String CHANNEL_MESSAGE_ID = "102";
    public static final String CHANNEL_MESSAGE_NAME = "MESSAGE";
    public static final String CHANNEL_MESSAGE_DESCRIPTION = "The channel is going to notify the message";

    public NotificationManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel_message = new NotificationChannel(CHANNEL_MESSAGE_ID, CHANNEL_MESSAGE_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel_message.enableVibration(true);
        manager.createNotificationChannel(channel_message);
        Intent intent=getIntent();
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        uid = intent.getStringExtra("uid");
        contact = intent.getStringExtra("contactName");
        sendMessage=findViewById(R.id.sendMessage);
        messageBox=findViewById(R.id.messageBox);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ALLSESSIONS);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot sessionSnapshot:dataSnapshot.getChildren()){
                    if(!(sessionSnapshot.getKey().equals("Length"))&&isSameUsers(sessionSnapshot)){
                        lastMessage=(Long)sessionSnapshot.child("MLength").getValue();
                        messages.clear();
                        for(DataSnapshot messageSnapshot:sessionSnapshot.child("History").getChildren()){
                            String content=(String)messageSnapshot.child("content").getValue();
                            String sender=(String)messageSnapshot.child("sender").getValue();
//                            String contactName = getOtherUsername(sessionSnapshot);
                            long myTimeStamp=(long)messageSnapshot.child("timeStamp").getValue();
                            Message message;
                            if(sender.equals(currentUid)) message=new Message(content, sender, myTimeStamp, TRUE, contact);
                            else message=new Message(content, sender, myTimeStamp, FALSE, contact);
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
                        sendNotification();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DatabaseReference newMyRef = database.getReference(ALLSESSIONS).child(sessionKey);
                    String sender=currentUid;
                    String content=messageBox.getText().toString().trim();
                    long currentTime=System.currentTimeMillis();
                    Message message = new Message(content, sender,currentTime, currentUserName);
                    Map<String, Object> sendMyMessage=new HashMap<>();
                    String key=++lastMessage+"";
                    key="Message"+key;
                    sendMyMessage.put(key,message);
                    newMyRef.child("History").updateChildren(sendMyMessage);
                    newMyRef.child("MLength").setValue(lastMessage);
                    messageBox.setText("");
                    lastMessage=0;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean isSameUsers(DataSnapshot sessionSnapshot){
        String uidA=(String) sessionSnapshot.child("UId1").getValue();
        String uidB=(String) sessionSnapshot.child("UId2").getValue();
        if ((currentUid.equals(uidA))&&uid.equals(uidB)||(currentUid.equals(uidB)&&uid.equals(uidA))){
            sessionKey=sessionSnapshot.getKey();
            return true;
        }
        else return false;
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

    public void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "102");
        builder.setTicker("Hello!");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(false);

        builder.setContentTitle("New Message");
        builder.setContentText("You have received a new message!");
        Intent intent = new Intent(this, ChatRoom.class);//点击通知进入哪个画面
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        manager.notify(2,builder.build());

    }
}
