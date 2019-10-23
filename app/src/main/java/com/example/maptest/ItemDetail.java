package com.example.maptest;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ItemDetail extends AppCompatActivity {
    private String currentUid;
    private String currentUsername;
    private String uid;
    private String name;
    private String description;
    private String contact;
    private String phone;
    private String image;
    private TextView item_name;
    private TextView item_description;
    private TextView item_publisher;
    private TextView item_contact;
    private TextView item_phone;
    private ImageView item_image;
    private Button start_chat;
    private final String ALLSESSIONS="AllSessions";
    private FirebaseDatabase database;
    private String sessionKey=null;

    private Bitmap bp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        name=intent.getStringExtra("name");
        description=intent.getStringExtra("description");
        contact=intent.getStringExtra("contactName");
        phone=intent.getStringExtra("Phone");
        image=intent.getStringExtra("image");
        setContentView(R.layout.item_detail);
        item_name=(TextView)findViewById(R.id.item_detail_name);
        item_description=(TextView)findViewById(R.id.item_detail_description);
        item_contact=(TextView)findViewById(R.id.item_contact);
        item_phone=(TextView)findViewById(R.id.item_phone);
        item_image=(ImageView)findViewById(R.id.item_detail_image);
        start_chat=(Button)findViewById(R.id.start_chat);
        item_name.setText(name);
        item_description.setText(description);
        if (image == null || image.equals("null")){
            item_image.setImageResource(R.drawable.noimage);
        }
        else{
            stringToBitmap(image);
            item_image.setImageBitmap(bp);
        }

        item_contact.setText(contact);
        item_phone.setText(phone);
        start_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    currentUid=currentUser.getUid();
                    currentUsername = currentUser.getDisplayName();
                    database = FirebaseDatabase.getInstance();
                    if (currentUid.equals(uid)){Toast.makeText(ItemDetail.this, "Hey you publish this item yourself!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DatabaseReference myRef = database.getReference(ALLSESSIONS);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean neverChat=true;
                                Message message=new Message();
                                for(DataSnapshot sessionSnapshot: dataSnapshot.getChildren()){
                                    if (!(sessionSnapshot.getKey().equals("Length"))&&isSameUsers(sessionSnapshot)){
                                        neverChat=false;
                                    }
                                }
                                if(neverChat){
                                    DatabaseReference newMyRef = database.getReference(ALLSESSIONS);
                                    long sessionNum=(long)dataSnapshot.child("Length").getValue();
                                    String key = "Session"+(++sessionNum);
                                    newMyRef.child("Length").setValue(sessionNum);
                                    Map<String, Object> holder =new HashMap<>();
                                    holder.put(key,"holder");
                                    newMyRef.updateChildren(holder);
                                    holder.clear();
                                    newMyRef = database.getReference(ALLSESSIONS).child(key);
                                    holder.put("MLength", 1);
                                    holder.put("UId1", currentUid);
                                    holder.put("Username1", currentUsername);
                                    holder.put("UId2", uid);
                                    holder.put("Username2", contact);
                                    holder.put("History", "holder");
                                    newMyRef.updateChildren(holder);
                                    holder.clear();
                                    newMyRef = database.getReference(ALLSESSIONS).child(key).child("History");
                                    message = new Message("Hola", currentUid, System.currentTimeMillis(), currentUsername);
                                    holder.put("Message1", message);
                                    newMyRef.updateChildren(holder);
                                }
                                Intent intent = new Intent(ItemDetail.this, ChatRoom.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("contactName", contact);
                                startActivity(intent);
                                ItemDetail.this.finish();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });}
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetail.this);
                    builder.setTitle("Please Login First!!!");
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
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

    public void stringToBitmap(String image){
        byte[] data = Base64.decode(image, Base64.DEFAULT);
        bp = BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
