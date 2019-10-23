package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPagerGuide;
    private int[] image;

    private ViewGroup group;
    private List<View> viewList;

    private Button start;

    private ImageView imageView;
    private ImageView[] imageViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        start = findViewById(R.id.goToMain);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewPagerInit();
        pointInit();


    }

    public void viewPagerInit(){
         viewPagerGuide = findViewById(R.id.viewpager_guide);
         image = new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
         viewList = new ArrayList<>();
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
         int size = image.length;
         for (int i = 0; i < size; i++){
             ImageView tempImageView = new ImageView(this);
             tempImageView.setLayoutParams(params);
             tempImageView.setBackgroundResource(image[i]);
             viewList.add(tempImageView);
         }

         viewPagerGuide.setAdapter(new AdapterForViewPager(viewList));
         viewPagerGuide.setOnPageChangeListener(this);

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

    public void onPageScrolled(int position, float offset, int offSetPixels){

    }

    public void onPageSelected(int index){
        int size = image.length;
        for (int i = 0; i < size; i++){
            imageViewArray[i].setBackgroundResource(R.drawable.dot_black);
            if (index != i){
                imageViewArray[i].setBackgroundResource(R.drawable.dot_white);
            }
            if (index -1 == size){
                start.setVisibility(View.VISIBLE);
            }
            else{
                start.setVisibility(View.GONE);
            }
        }
    }

    public void onPageScrollStateChanged(int state){

    }
}
