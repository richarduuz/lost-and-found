package com.example.maptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private mapFragment map_fragment;
    private listFragment list_fragment;
    private MeFragment meFragment;
    private chatListFragment chat_list_fragment;
    public static String targetBuilding = "Doug McDonell";
//    private MessageFragment messageFragment;
//    private MeFragment meFragment;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentAdapter fragmentAdpater;
    private BottomNavigationView btmMenu;

    private String[] FragmentTitles = {"Map", "List", "Message", "Me"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentInit();

        fragmentAdpater = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdpater);

        viewPager.setOffscreenPageLimit(4);

        viewPager.setCurrentItem(0);

        btmMenu = findViewById(R.id.bottom_menu);
        btmMenu.setOnNavigationItemSelectedListener(btmNavigationSelectedListener);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2){
                    switch (viewPager.getCurrentItem()){
                        case 0:
                            btmMenu.setSelectedItemId(R.id.navigation_map);
                            break;
                        case 1:
                            btmMenu.setSelectedItemId(R.id.navigation_list);
                            break;
                        case 2:
                            btmMenu.setSelectedItemId(R.id.navigation_message);
                            break;
                        case 3:
                            btmMenu.setSelectedItemId(R.id.navigation_me);
                            break;
                    }
                }
            }
        });








    }

    private BottomNavigationView.OnNavigationItemSelectedListener btmNavigationSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            // when click the menu, switch to specific fragment
            switch (menuItem.getItemId()){
                case R.id.navigation_map:{
                    viewPager.setCurrentItem(0);
                    return true;
                }
                case R.id.navigation_list:{
                    viewPager.setCurrentItem(1);
                    return true;
                }
                case R.id.navigation_message:{
                    viewPager.setCurrentItem(2);
                    return true;
                }
                case R.id.navigation_me:{
                    viewPager.setCurrentItem(3);
                    return true;
                }
            }
            return false;
        }
    };





    public void fragmentInit(){
        viewPager = findViewById(R.id.mainViewPager);
        map_fragment = new mapFragment();
        meFragment = new MeFragment();
        list_fragment = new listFragment();
        chat_list_fragment = new chatListFragment();
        fragmentList.add(map_fragment);
        fragmentList.add(list_fragment);
        fragmentList.add(chat_list_fragment);
        fragmentList.add(meFragment);


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
