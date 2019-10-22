package com.example.maptest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.ArrayList;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link chatListFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link chatListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class chatListFragment extends Fragment {
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
    private String currentUid=null;
    private String TAG = "MAPTEST";
    private ArrayList<ChatUser> allUsers= new ArrayList<>();
    private AllChatRoomsAdapter mAdapter = null;
    private ListView listView;
    private final String ALLSESSIONS="AllSessions";
    private String sessionKey=null;
    private String uid=null;

    public chatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser==null){
        }else{
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ALLSESSIONS);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (chatListFragment.this.getActivity()!=null){
                allUsers.clear();
                for (DataSnapshot chatWithuser: dataSnapshot.getChildren()){
                    if (!(chatWithuser.getKey().equals("Length"))&&containedUser(chatWithuser)){
                        String chatUid = uid;
                        String chatUsername="Chang LIU";
                        int userIcon=R.drawable.noimage;
                        allUsers.add(new ChatUser(chatUid,chatUsername,userIcon));
                    }
                }
                listView=(ListView) view.findViewById(R.id.chat_list);
                try{
                mAdapter = new AllChatRoomsAdapter(chatListFragment.this.getActivity(), R.layout.list_layout, allUsers);}
                catch(NullPointerException e){
                    System.out.println(chatListFragment.this.getActivity().toString());
                }
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ChatUser user=(ChatUser) parent.getItemAtPosition(position);
                        String userId = user.getUid();
                        Intent intent = new Intent(chatListFragment.this.getActivity(), ChatRoom.class);
                        intent.putExtra("uid", userId);
                        startActivity(intent);
                    }
                });
            }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        return view;
    }

    public boolean containedUser(DataSnapshot chatWithuser){
        String uidA=(String) chatWithuser.child("UId1").getValue();
        String uidB=(String) chatWithuser.child("UId2").getValue();
        if (currentUid.equals(uidA)||currentUid.equals(uidB)){
            sessionKey=chatWithuser.getKey();
            uid=currentUid.equals(uidA)? uidB:uidA;
            return true;
        }
        else return false;
    }
}
