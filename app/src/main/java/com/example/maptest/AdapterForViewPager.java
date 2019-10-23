package com.example.maptest;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class AdapterForViewPager extends PagerAdapter {
    public List<View> viewList;

    public AdapterForViewPager(List<View> views){
        this.viewList = views;
    }

    public int getCount(){
        return viewList.size();
    }

    public Object instantiateItem(ViewGroup container, int position){
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView(viewList.get(position));
    }

    public boolean isViewFromObject(View arg0, Object arg1){
        return arg0 == arg1;
    }
}
