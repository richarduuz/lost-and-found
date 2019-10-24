package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Guide extends AppCompatActivity {
    private Button button;
    private ViewPager viewPagerGuide;
    private int[] image;

    private ViewGroup group;
    private ArrayList<Fragment> viewList;
    private FragmentAdapter fragmentAdpater;

    private Button start;

    private ImageView imageView;
    private ImageView[] imageViewArray;
    private guideFragment1 guide1;
    private guideFragment2 guide2;
    private guideFragment3 guide3;
    private guideFragment5 guide5;
//    private guideFragment4 guide4;
    private guideFragment6 guide6;
    private guideFragment7 guide7;
    private guideFragment8 guide8;
    private guideFragment9 guide9;



    public class FragmentAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> List){
            super(fm);
            this.list = List;

        }

        public Fragment getItem(int number){
            return list.get(number);
        }

        public int getCount(){
            return list.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide2);

        init();
        fragmentAdpater = new FragmentAdapter(Guide.this.getSupportFragmentManager(), viewList);
        viewPagerGuide.setAdapter(fragmentAdpater);

        viewPagerGuide.setOffscreenPageLimit(7);
        viewPagerGuide.setCurrentItem(0);

        viewPagerGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int index) {
                int size = viewList.size();
                for (int i = 0; i < size; i++){
                    imageViewArray[i].setBackgroundResource(R.drawable.dot_white);
                    if (index != i){
                        imageViewArray[i].setBackgroundResource(R.drawable.dot_black);
                    }
                    if (index +1 == size){
                        start.setText("START");
                        start.setVisibility(View.VISIBLE);
                    }
                    else if (index == 0){
                        start.setText("SKIP GUIDE");
                        start.setVisibility(View.VISIBLE);
                    }
                    else{
                        start.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        start = findViewById(R.id.goToMain);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Guide.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        pointInit();


    }

    public void init(){
        viewPagerGuide = findViewById(R.id.viewPagerGuide);
        guide1 = new guideFragment1();
        guide2 = new guideFragment2();
        guide3 = new guideFragment3();
        guide8 = new guideFragment8();
        guide9 = new guideFragment9();
//        guide4 = new guideFragment4();
        guide5 = new guideFragment5();
        guide6 = new guideFragment6();
        guide7 = new guideFragment7();

        viewList = new ArrayList<>();
        viewList.add(guide1);
        viewList.add(guide2);
        viewList.add(guide3);
        viewList.add(guide8);
        viewList.add(guide9);
        viewList.add(guide5);
        viewList.add(guide6);
        viewList.add(guide7);
    }

    public void pointInit(){
        group = findViewById(R.id.guideLayout);
        imageViewArray = new ImageView[viewList.size()];
        int len  = viewList.size();
        for (int i = 0; i < len; i++){
            imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
            layoutParams.leftMargin = 30;
            layoutParams.rightMargin = 30;
            imageView.setLayoutParams(layoutParams);

            imageViewArray[i] = imageView;
            if (i == 0){
                imageView.setBackgroundResource(R.drawable.dot_white);
            }
            else{
                imageView.setBackgroundResource(R.drawable.dot_black);
            }
            group.addView(imageViewArray[i]);
        }
    }


}
