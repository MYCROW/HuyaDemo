package com.example.crow.huyademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.crow.huyademo.customview.CategoryTabStrip;
import com.example.crow.huyademo.fragment.BlankFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CategoryTabStrip tabs;
    private ViewPager pager;
    private TopPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //底部选项卡响应
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //顶部选项卡响应
        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new TopPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        //检查授权
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //顶部选项卡适配器
    public class TopPagerAdapter extends FragmentPagerAdapter {

        private final List<String> catalogs = new ArrayList<String>();

        public TopPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add(getString(R.string.top_recommand));
            catalogs.add(getString(R.string.top_recommand1));
            catalogs.add(getString(R.string.top_recommand2));
            catalogs.add(getString(R.string.top_recommand3));
            catalogs.add(getString(R.string.top_recommand4));
            catalogs.add(getString(R.string.top_recommand5));
            catalogs.add(getString(R.string.top_recommand6));
            catalogs.add(getString(R.string.top_recommand7));
            catalogs.add(getString(R.string.top_recommand8));
            catalogs.add(getString(R.string.top_recommand9));
            catalogs.add(getString(R.string.top_recommand10));
            catalogs.add(getString(R.string.top_recommand11));
            catalogs.add(getString(R.string.top_recommand12));
            catalogs.add(getString(R.string.top_recommand13));
            catalogs.add(getString(R.string.top_recommand14));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return BlankFragment.newInstance(position);
        }
    }

    //底部选项卡监听事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
            return false;
        }
    };
}
