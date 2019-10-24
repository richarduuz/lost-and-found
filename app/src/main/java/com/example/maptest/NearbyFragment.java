package com.example.maptest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class NearbyFragment extends Fragment {

    private SectionPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;


    public NearbyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.tab_container);
        setupViewPager(mViewPager);

        mSectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new NearbyLost(), "Lost");
        adapter.addFragment(new NearbyFound(), "Found");
        viewPager.setAdapter(adapter);
    }
}
