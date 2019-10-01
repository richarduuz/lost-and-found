package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Building_Items extends AppCompatActivity {


    private TextView title, item_lost, item_found;
    private ViewPager vp;
    private LostFragment lostFragment;
    private FoundFragment foundFragment;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentAdapter fragmentAdpater;

    private String[] titles = {"Lost", "Found"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_building__items);


        view_Init();
        fragmentAdpater = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp.setAdapter(fragmentAdpater);
        vp.setOffscreenPageLimit(2);//ViewPager的缓存为4帧

        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void view_Init(){
        title = findViewById(R.id.title);
        item_found = findViewById(R.id.lost);
        item_lost = findViewById(R.id.found);

        vp =  findViewById(R.id.mainViewPager);

        lostFragment = new LostFragment();
        foundFragment = new FoundFragment();

        fragmentList.add(this.lostFragment);
        fragmentList.add(this.foundFragment);
    }

    public class FragmentAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> list = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> List){
            super(fm);
            this.list = List;

        }

        public Fragment getItem(int number){
            return fragmentList.get(number);
        }

        public int getCount(){
            return list.size();
        }



    }

}
