package com.example.maptest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class guideFragment9 extends Fragment {

    private ImageView imageView;

    public guideFragment9() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide_fragment9, container, false);
        imageView = view.findViewById(R.id.guideImage9);
        imageView.setImageResource(R.drawable.guide9);
        return view;
    }

}
