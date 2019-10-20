package com.example.maptest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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

    private ArrayList<PublishItem> items=new ArrayList<>();
    private ListView listView;
    private ListDemoAdapter mAdapter=null;
    private Button GotoPublish;
    private Button GotoFound;
    private String Found;
    private String Location = MainActivity.targetBuilding;



    public listFragment() {
        // Required empty public constructor
    }



//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment listFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static listFragment newInstance(String param1, String param2) {
//        listFragment fragment = new listFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, true);
        GotoPublish = view.findViewById(R.id.publish);
        GotoFound = view.findViewById(R.id.toFound);
        listView =  view.findViewById(R.id.item_list);
        Found = "False";
        if (Found.equals("False")) GotoFound.setText("Go to found list");
        else GotoFound.setText("Go to lost list");
        fetchData();


        GotoPublish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(listFragment.this.getActivity(), GoToPublishItems.class);
                intent.putExtra("Location", Location);
                intent.putExtra("Found",Found);
                startActivity(intent);
            }
        });

        GotoFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Found.equals("False")){
                    Found = "True";
                }
                else{
                    Found = "False";
                }
                if (Found.equals("False")) GotoFound.setText("Go to found list");
                else GotoFound.setText("Go to lost list");
                items.clear();
                fetchData();
            }
        });
        return view;
    }

    public void fetchData(){
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
                            System.out.println("ready for content");
                            listView.setAdapter(mAdapter);
                        }
                    }
                });
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}