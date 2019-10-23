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

        viewPagerGuide.setOffscreenPageLimit(3);
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
        viewList = new ArrayList<>();
        viewList.add(guide1);
        viewList.add(guide2);
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
