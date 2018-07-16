package com.example.crow.huyademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //顶部选项卡响应
        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new TopPagerAdapter(getSupportFragmentManager());

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

        private List<String> catalogs = new ArrayList<String>();

        public class LoadTopData extends AsyncTask<Integer, Integer, ArrayList<String>> {
            // doInBackground（）
            // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
            // 注：必须复写，从而自定义线程任务
            @Override
            protected ArrayList<String> doInBackground(Integer... params) {
                ArrayList<String> data = new ArrayList<>();
                data.add(getString(R.string.top_recommand));
                data.add(getString(R.string.top_recommand1));
                data.add(getString(R.string.top_recommand2));
                data.add(getString(R.string.top_recommand3));
                data.add(getString(R.string.top_recommand4));
                data.add(getString(R.string.top_recommand5));
                data.add(getString(R.string.top_recommand6));
                data.add(getString(R.string.top_recommand7));
                data.add(getString(R.string.top_recommand8));
                data.add(getString(R.string.top_recommand9));
                data.add(getString(R.string.top_recommand10));
                data.add(getString(R.string.top_recommand11));
                data.add(getString(R.string.top_recommand12));
                data.add(getString(R.string.top_recommand13));
                data.add(getString(R.string.top_recommand14));
                return data;
            }

            // onPostExecute（）
            // 作用：接收线程任务执行结果、将执行结果显示到UI组件
            // 注：必须复写，从而自定义UI操作
            @Override
            protected void onPostExecute(ArrayList<String> result) {
                catalogs = result;
                if(pager.getAdapter()==null) {
                    pager.setAdapter(adapter);
                    tabs.setViewPager(pager);
                }
                else
                    adapter.notifyDataSetChanged();
            }
        }

        public TopPagerAdapter(FragmentManager fm) {
            super(fm);
            LoadTopData task = new LoadTopData();
            task.execute();
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
