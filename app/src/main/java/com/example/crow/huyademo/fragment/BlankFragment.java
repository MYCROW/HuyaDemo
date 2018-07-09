package com.example.crow.huyademo.fragment;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.crow.huyademo.MainActivity;
import com.example.crow.huyademo.R;
import com.example.crow.huyademo.customview.VerticalViewPager;
import com.example.crow.huyademo.thread.HttpImgThread;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BlankFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;

    private static String NUM = "0";
    private int num;

    Context context;
    View view;
    RecyclerView mRecyclerView;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(int number) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(NUM, number + "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num = Integer.parseInt(getArguments().getString(NUM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        if (num == 0) { //推荐
            view = inflater.inflate(R.layout.fragment_index0, container, false);
            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listview
            mRecyclerView.setAdapter(new RecyclerViewAdapter(context));
        } else {
            view = inflater.inflate(R.layout.fragment_index1, container, false);
            TextView tx = view.findViewById(R.id.test1);
            tx.setText("" + num);
        }
        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewGroupHolder> {
        private final LayoutInflater mLayoutInflater;
        private final Context mContext;

        private ArrayList<ShowData> data;
        //ShowData 展示数据
        final int SEARCH = 0;
        final int LIVE = 1;
        final int ADBORAD = 2;
        final int ACTIVI = 3;
        final int ADLIST = 4;
        final int DEFAULTTYPE = SEARCH;

        public class ShowData {
            int index;
            int datatype;
            boolean hasData;

            public ShowData(int type,int index) {
                if (type != SEARCH &&
                        type != LIVE &&
                        type != ADBORAD &&
                        type != ACTIVI &&
                        type != ADLIST)
                    datatype = DEFAULTTYPE;
                else
                    datatype = type;
                hasData = false;
                this.index = index;
            }

            //Search data 搜索框数据
            SearchData searchData;

            public class SearchData {
                public String bgtext;//搜索框背景文字
                public boolean hasScanBtn;//是否有扫描二维码按钮
            }

            void addDataSearch(String bgtext, boolean hasScanBtn) {
                if (datatype != SEARCH || hasData == true)
                    return;
                searchData = new SearchData();
                searchData.bgtext = bgtext;
                searchData.hasScanBtn = hasScanBtn;
                hasData = true;
            }

            //Live list data直播列表数据
            LiveListData liveListData;

            public class LiveListData {
                public String listtitle;
                public String[] titles;//标题
                public String[] urls;//预览图
                public String[] liveurl;
                public String[] tabs;//直播间标签
                public String[] name;//直播间名
                public String[] amount;//直播间人数
                public int size;//列表尺寸
            }

            void addDataLivelist(String listtitle, String[] titles, String[] urls, String[] liveurl, String[] tabs, String[] name, String[] amount, int size) {
                if (datatype != LIVE || hasData == true)
                    return;
                liveListData = new LiveListData();
                liveListData.listtitle = listtitle;
                liveListData.titles = titles;
                liveListData.urls = urls;
                liveListData.liveurl = liveurl;
                liveListData.tabs = tabs;
                liveListData.name = name;
                liveListData.amount = amount;
                if (size <= 6)
                    liveListData.size = size;
                else
                    liveListData.size = 6;
                hasData = true;
            }

            //Advertisment board data 广告板数据
            AdverboardData adverData;

            public class AdverboardData {
                public String[] imageUrl;
                public String[] targetUrl;
                public int[] index;
                public String[] tag;
            }

            void addDataAdver(String[] imageUrl, String[] targetUrl, int[] index, String[] tag) {
                if (datatype != ADBORAD || hasData == true)
                    return;
                adverData = new AdverboardData();
                adverData.imageUrl = imageUrl;
                adverData.targetUrl = targetUrl;
                adverData.index = index;
                adverData.tag = tag;
                hasData = true;
            }

            //Activites data 活动数据
            ActivityData activityData;

            public class ActivityData {
                public String[] titles;
                public String[] text;
                public String[] numberOrder;
                public String[] targetUrl;
                public boolean[] hasOrder;
            }

            void addDataActivity(String[] titles, String[] text, String[] numberOrder, String[] targetUrl, boolean[] hasOrder) {
                if (datatype != ACTIVI || hasData == true)
                    return;
                activityData = new ActivityData();
                activityData.titles = titles;
                activityData.text = text;
                activityData.numberOrder = numberOrder;
                activityData.targetUrl = targetUrl;
                activityData.hasOrder = hasOrder;
                hasData = true;
            }

            //Advertisment list data 广告列表数据
            AdverlistData adverlistData;

            public class AdverlistData{
                public String listtitle;
                public String[] imageUrl;
                public String[] targetUrl;
                public String[] advertitle;
                public String[] advertext;
            }

            void addDataAdverList(String listtitle, String[] imageUrl,String[] targetUrl, String[] advertitle, String[] advertext){
                if (datatype != ADLIST || hasData == true)
                    return;
                adverlistData = new AdverlistData();
                adverlistData.listtitle = listtitle;
                adverlistData.imageUrl = imageUrl;
                adverlistData.targetUrl = targetUrl;
                adverlistData.advertitle = advertitle;
                adverlistData.advertext = advertext;
                hasData = true;
            }


            public int getType() {
                return datatype;
            }

            public Object getData() {
                if (hasData == false)
                    return null;
                if (datatype == SEARCH)
                    return searchData;
                if (datatype == LIVE)
                    return liveListData;
                if (datatype == ADBORAD)
                    return adverData;
                if (datatype == ACTIVI)
                    return activityData;
                if (datatype == ADLIST)
                    return adverlistData;
                return null;
            }
        }

        //start thread for load data
        public RecyclerViewAdapter(Context context) {
            //mTitles = context.getResources().getStringArray(R.array.titles);
            data = new ArrayList<>(10);
            //添加搜索框项
            LoadDataTask task1 = new LoadDataTask();
            task1.execute(SEARCH,0,0);
//            ShowData itemdata0 = new ShowData(SEARCH);
//            itemdata0.addDataSearch("text", true);
//            data.add(0, itemdata0);
            //添加广告版项
            LoadDataTask task2 = new LoadDataTask();
            task2.execute(ADBORAD,1,0);
//            ShowData itemdata1 = new ShowData(ADBORAD);
//            String[] imageUrl = {"http://img.sccnn.com/bimg/338/50000.jpg", "", "", ""};
//            String[] targetUrl = {"1", "1", "1", "1"};
//            int[] index = {0, 1, 2, 3};
//            String[] tag = {"广告1", "推广", "广告2", "广告3"};
//            itemdata1.addDataAdver(imageUrl, targetUrl, index, tag);
//            data.add(1, itemdata1);
            //添加直播列表项
            LoadDataTask task3 = new LoadDataTask();
            task3.execute(LIVE,2,0);
//            ShowData itemdata2 = new ShowData(LIVE);
//            String listtitle = "全部直播";
//            String[] titles = {"标题1", "标题2", "标题3", "标题4", "标题5", "标题6"};
//            String[] imageUrl2 = {"http://img.sccnn.com/bimg/338/50000.jpg",
//                    "https://p9.pstatp.com/weili/l/79053546024309963.webp",
//                    "http://pic1.ooopic.com/uploadfilepic/sheying/2009-10-29/OOOPIC_wcl00124_2009102992c2017a3e978ef7.jpg", "", "", ""};
//            String[] liveUrl = {"", "", "", "", "", ""};
//            String[] livetag = {"绝地求生", "刺激战场", "暴雪专区", "LOL", "绝地求生", "绝地求生"};
//            String[] livename = {"二丫", "DGTY", "Nova", "青蛙", "韦神", "二大爷"};
//            String[] amount = {"1.1万", "5万", "4500", "11万", "902", "4111"};
//            int size = 6;
//            itemdata2.addDataLivelist(listtitle, titles, imageUrl2, liveUrl, livetag, livename, amount, size);
//            data.add(2, itemdata2);
//            //添加活动项
            LoadDataTask task4 = new LoadDataTask();
            task4.execute(ACTIVI,3,0);
//            ShowData itemdata3 = new ShowData(ACTIVI);
//            String[] titlesActivity = {"KPL2018春季赛", "QQ飞车手游新版上线", "LOL洲际赛对抗赛"};
//            String[] textActivity = {"今天20点开始", "今天15点开始", "今天9点开始"};
//            String[] numberOrder = {"10000", "100000", "10000"};
//            String[] targetUrl2 = {"", "", ""};
//            boolean[] hasOrder = {true, true, true, true};
//            itemdata3.addDataActivity(titlesActivity, textActivity, numberOrder, targetUrl2, hasOrder);
//            data.add(3, itemdata3);
//
//            //添加直播列表项2
            LoadDataTask task5 = new LoadDataTask();
            task5.execute(LIVE,4,1);
//            ShowData itemdata4 = new ShowData(LIVE);
//            String listtitle2 = "绝地求生";
//            String[] titles2 = {"标题1", "标题2", "标题3", "标题4", "标题5", "标题6"};
//            String[] imageUrl3 = {"http://img.sccnn.com/bimg/338/50000.jpg",
//                    "https://p9.pstatp.com/weili/l/79053546024309963.webp",
//                    "http://pic1.ooopic.com/uploadfilepic/sheying/2009-10-29/OOOPIC_wcl00124_2009102992c2017a3e978ef7.jpg", "", "", ""};
//            String[] liveUrl2 = {"", "", "", "", "", ""};
//            String[] livetag2 = {"", "", "", "", "", ""};
//            String[] livename2 = {"二丫", "DGTY", "Nova", "青蛙", "韦神", "二大爷"};
//            String[] amount2 = {"1.1万", "5万", "4500", "11万", "902", "4111"};
//            int size2 = 6;
//            itemdata4.addDataLivelist(listtitle2, titles2, imageUrl3, liveUrl2, livetag2, livename2, amount2, size2);
//            data.add(4, itemdata4);
//
//            //添加广告列表项
            LoadDataTask task6 = new LoadDataTask();
            task6.execute(ADLIST,5,0);
//            ShowData itemdata5 = new ShowData(ADLIST);
//            String listtitle3 = "最新热游";
//            String[] imageUrl4  = {"","","","","",""};
//            String[] targetUrl4 = {"","","","","",""};
//            String[] advertitle = {"刺激战场","逆水寒","全军出击","本周新游","QQ飞车","创造与魔法"};
//            String[] advertext = {"10000人在播","5555人在播","666人在播","7777人在播","8人在播","10人在播"};
//            itemdata5.addDataAdverList(listtitle3,imageUrl4,targetUrl4,advertitle,advertext);
//            data.add(5,itemdata5);

            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        //prepare data
        public void prepareSearchData(ShowData data,int dataid){
            if(data.datatype != SEARCH)
                return;
            data.addDataSearch("text", true);
        }
        public void prepareAdverboardData(ShowData data,int dataid){
            String[] imageUrl = {"http://img.sccnn.com/bimg/338/50000.jpg",
                    "http://img.sccnn.com/bimg/338/50105.jpg",
                    "http://img.sccnn.com/bimg/341/05764.jpg",
                    "http://img.sccnn.com/bimg/338/50104.jpg"};
            String[] targetUrl = {"1", "1", "1", "1"};
            int[] index = {0, 1, 2, 3};
            String[] tag = {"广告1", "推广", "广告2", "广告3"};
            data.addDataAdver(imageUrl, targetUrl, index, tag);
        }
        public void prepareLiveData(ShowData data,int dataid){
            String listtitle = "全部直播";
            String[] titles = {"标题1", "标题2", "标题3", "标题4", "标题5", "标题6"};
            String[] imageUrl2 = {"http://img.sccnn.com/bimg/338/50000.jpg",
                    "https://p9.pstatp.com/weili/l/79053546024309963.webp",
                    "http://pic1.ooopic.com/uploadfilepic/sheying/2009-10-29/OOOPIC_wcl00124_2009102992c2017a3e978ef7.jpg", "", "", ""};
            String[] liveUrl = {"", "", "", "", "", ""};
            String[] livetag = {"绝地求生", "刺激战场", "暴雪专区", "LOL", "绝地求生", "绝地求生"};
            String[] livename = {"二丫", "DGTY", "Nova", "青蛙", "韦神", "二大爷"};
            String[] amount = {"1.1万", "5万", "4500", "11万", "902", "4111"};
            int size = 6;
            data.addDataLivelist(listtitle, titles, imageUrl2, liveUrl, livetag, livename, amount, size);
        }

        public void prepareActivity(ShowData data,int dataid){
            String[] titlesActivity = {"KPL2018春季赛", "QQ飞车手游新版上线", "LOL洲际赛对抗赛"};
            String[] textActivity = {"今天20点开始", "今天15点开始", "今天9点开始"};
            String[] numberOrder = {"10000", "100000", "10000"};
            String[] targetUrl2 = {"", "", ""};
            boolean[] hasOrder = {true, true, true, true};
            data.addDataActivity(titlesActivity, textActivity, numberOrder, targetUrl2, hasOrder);
        }

        public void prepareAdverlistData(ShowData data,int dataid){
            String listtitle3 = "最新热游";
            String[] imageUrl4  = {"","","","","",""};
            String[] targetUrl4 = {"","","","","",""};
            String[] advertitle = {"刺激战场","逆水寒","全军出击","本周新游","QQ飞车","创造与魔法"};
            String[] advertext = {"10000人在播","5555人在播","666人在播","7777人在播","8人在播","10人在播"};
            data.addDataAdverList(listtitle3,imageUrl4,targetUrl4,advertitle,advertext);
        }

        public class LoadDataTask extends AsyncTask<Integer, Integer, ShowData> {
            // doInBackground（）
            // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
            // 注：必须复写，从而自定义线程任务
            @Override
            protected ShowData doInBackground(Integer... params) {
                ShowData data;
                int index = params[1];
                int dataid = params[2];
                switch (params[0]){
                    case SEARCH:
                        data = new ShowData(SEARCH,index);
                        prepareSearchData(data,dataid);
                        break;
                    case LIVE:
                        data = new ShowData(LIVE,index);
                        prepareLiveData(data,dataid);
                        break;
                    case ADBORAD:
                        data = new ShowData(ADBORAD,index);
                        prepareAdverboardData(data,dataid);
                        break;
                    case ACTIVI:
                        data = new ShowData(ACTIVI,index);
                        prepareActivity(data,dataid);
                        break;
                    case ADLIST:
                        data = new ShowData(ADLIST,index);
                        prepareAdverlistData(data,dataid);
                        break;
                    default:
                        data = new ShowData(SEARCH,index);
                        prepareSearchData(data,dataid);
                        break;
                }
                return data;
            }

            // onPostExecute（）
            // 作用：接收线程任务执行结果、将执行结果显示到UI组件
            // 注：必须复写，从而自定义UI操作
            @Override
            protected void onPostExecute(ShowData result) {
                data.add(result.index,result);
                notifyDataSetChanged();
            }
        }

        @Override
        public ViewGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewGroupHolder(mLayoutInflater.inflate(R.layout.item_group, parent, false), context);
        }

        @Override
        public void onBindViewHolder(ViewGroupHolder holder, int position) {
            //Log.i("onBindViewHolder",data.size()+"");
            holder.show(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        public class ViewGroupHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
            ViewGroup parentview;
            View itemview;
            private final LayoutInflater itemInflater;
            Handler handler;

            ViewGroupHolder(View view, Context context) {
                super(view);
                parentview = (ViewGroup) view;
                itemInflater = LayoutInflater.from(context);
                handler = new Handler();
            }

            public void show(ShowData data) {
                switch (data.getType()) {
                    case SEARCH:
                        itemview = itemInflater.inflate(R.layout.item_search, parentview, false);
                        SearchView searchView = itemview.findViewById(R.id.searchview);
                        Button scanbutton = itemview.findViewById(R.id.scanbt);
                        ShowData.SearchData searchData = (ShowData.SearchData) data.getData();
                        //扫描按钮
                        if (searchData.hasScanBtn == false) {
                            scanbutton.setVisibility(View.GONE);
                        } else {
                            scanbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //调用摄像头
                                }
                            });
                        }
                        //搜索框
                        searchView.setQueryHint("这里其实应该是按钮:" + searchData.bgtext);
                        break;
                    case LIVE:
                        itemview = itemInflater.inflate(R.layout.item_livelist, parentview, false);
                        ShowData.LiveListData liveData = (ShowData.LiveListData) data.getData();
                        showLivelist(itemview, context, liveData);
                        break;
                    case ADBORAD:
                        itemview = itemInflater.inflate(R.layout.item_adverboard, parentview, false);
                        ShowData.AdverboardData adboardData = (ShowData.AdverboardData) data.getData();
                        showAdverboard(itemview, context, adboardData);
                        break;
                    case ACTIVI:
                        itemview = itemInflater.inflate(R.layout.item_activity, parentview, false);
                        ShowData.ActivityData activityData = (ShowData.ActivityData) data.getData();
                        showActivity(itemview, context, activityData);
                        break;
                    case ADLIST:
                        itemview = itemInflater.inflate(R.layout.item_adverlist, parentview, false);
                        ShowData.AdverlistData adverlistData = (ShowData.AdverlistData) data.getData();
                        showAdverlist(itemview, context, adverlistData);
                        break;
                }
                parentview.addView(itemview);
            }

            /**
             * 根据传入数据展示 广告版
             * **/
            //TODO 为每一页广告根据targetURL绑定链接
            AdverboardHandler mHandler = new AdverboardHandler(new WeakReference<ViewGroupHolder>(this));
            ViewPager adverboardviewPager;
            TextView textView;
            List<ImageView> mImageList;
            List<RadioButton> mDotList;
            ShowData.AdverboardData adboardData;
            public void showAdverboard(View view, Context context, ShowData.AdverboardData adboardData) {
                this.adboardData = adboardData;
                String[] imageUrl = adboardData.imageUrl;
                String[] targetUrl = adboardData.targetUrl;
                int[] index = adboardData.index;
                String[] tag = adboardData.tag;
                //imageview 列表
                mImageList = new ArrayList<ImageView>();
                //放圆点的View的list
                mDotList = new ArrayList<RadioButton>();

                adverboardviewPager = view.findViewById(R.id.adverpager);
                textView = view.findViewById(R.id.advtext);
                RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
                //初始化右下圆点组视图
                radioGroup.removeAllViews();
                for (int i = 0; i < index.length; i++) {
                    RadioButton bt = new RadioButton(context);
                    bt.setEnabled(false);
                    radioGroup.addView(bt);
                    mDotList.add(bt);
                }
                //下载图片并保存到mImageList列表中
                for (int i = 0; i < index.length; i++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (imageUrl[index[i]] != "") {
                        HttpImgThread httpImgThread = new HttpImgThread(handler, imageView, imageUrl[index[i]]);
                        new Thread(httpImgThread).start();
                    } else {
                        Drawable bg = getResources().getDrawable(R.drawable.bg_live_img);
                        imageView.setBackgroundDrawable(bg);
                    }
                    mImageList.add(imageView);
                }
                //配置适配器等
                adverboardviewPager.setAdapter(new ImageAdapter(mImageList));
                adverboardviewPager.addOnPageChangeListener(new PageChangeListener());
                adverboardviewPager.setCurrentItem(0);
                mDotList.get(0).setChecked(true);
                adverboardviewPager.setFocusable(true);
                adverboardviewPager.setOnTouchListener(this);
                mHandler.sendEmptyMessageDelayed(AdverboardHandler.MSG_UPDATE_IMAGE, AdverboardHandler.MSG_DELAY);
            }
            //为广告版重写onTouch
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //test can reach
                if(v.equals(adverboardviewPager)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mHandler.sendEmptyMessage(AdverboardHandler.MSG_KEEP_SILENT);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mHandler.sendEmptyMessageDelayed(AdverboardHandler.MSG_UPDATE_IMAGE, AdverboardHandler.MSG_DELAY);
                            break;
                    }
                }
                else if(v.equals(activityviewPager)){
                    return true;
                }
                return false;
            }

            //广告版的 ViewPager适配器
            private class ImageAdapter extends PagerAdapter {
                private List<ImageView> viewlist;

                public ImageAdapter(List<ImageView> viewlist) {
                    this.viewlist = viewlist;
                }

                @Override
                public int getCount() {
                    // 设置成最大，使用户看不到边界
                    return Integer.MAX_VALUE;
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    // Warning：不要在这里调用removeView
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    // 对ViewPager页号求模取出View列表中要显示的项
                    position %= viewlist.size();
                    if (position < 0) {
                        position = viewlist.size() + position;
                    }
                    ImageView view = viewlist.get(position);
                    // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                    ViewParent vp = view.getParent();
                    if (vp != null) {
                        ViewGroup parent = (ViewGroup) vp;
                        parent.removeView(view);
                    }
                    container.addView(view);
                    // 此处可添加监听事件

                    return view;
                }
            }

            //广告版的 监听
            private class PageChangeListener implements ViewPager.OnPageChangeListener {
                // 配合Adapter的currentItem字段进行设置。
                @Override
                public void onPageSelected(int position) {
                    mHandler.sendMessage(Message.obtain(mHandler, AdverboardHandler.MSG_PAGE_CHANGED, position, 0));
                    position %= mDotList.size();
                    if (position < 0) {
                        position = mDotList.size() + position;
                    }
                    for(int i =0;i<mDotList.size();i++) {
                        if(i==position)
                            mDotList.get(i).setChecked(true);
                        else
                            mDotList.get(i).setChecked(false);
                    }
                    textView.setText(adboardData.tag[position]);
                }

                @Override
                public void onPageScrolled(int position, float arg1, int arg2) {
                }

                // 覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int state) {
                    //Log.i("onPageScrollState",state+"");
                    switch (state) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            mHandler.sendEmptyMessage(AdverboardHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            mHandler.sendEmptyMessageDelayed(AdverboardHandler.MSG_UPDATE_IMAGE, AdverboardHandler.MSG_DELAY);
                            break;
                        default:
                            break;
                    }
                }

            }

            //广告版的对应Handler 处理自动滚动的功能
            private class AdverboardHandler extends Handler {
                /**
                 * 请求更新显示的View。
                 */
                protected static final int MSG_UPDATE_IMAGE = 1;
                /**
                 * 请求暂停轮播。
                 */
                protected static final int MSG_KEEP_SILENT = 2;
                /**
                 * 请求恢复轮播。
                 */
                protected static final int MSG_BREAK_SILENT = 3;
                /**
                 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
                 * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
                 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
                 */
                protected static final int MSG_PAGE_CHANGED = 4;
                // 轮播间隔时间
                protected static final long MSG_DELAY = 3000;
                //  使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
                private WeakReference<ViewGroupHolder> weakReference;
                private int currentItem = 0;

                protected AdverboardHandler(WeakReference<ViewGroupHolder> wk) {
                    weakReference = wk;
                }

                @Override
                //TODO
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    final ViewGroupHolder viewGroupHolder = weakReference.get();
                    if (viewGroupHolder == null) {
                        // viewGroupHolder 已经回收，无需再处理UI了
                        return;
                    }
                    // 第一次不删重复的消息
                    if (currentItem != 0) {
                        // 检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
                        if (viewGroupHolder.mHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                            viewGroupHolder.mHandler.removeMessages(MSG_UPDATE_IMAGE);
                        }
                    }
                    switch (msg.what) {
                        case MSG_UPDATE_IMAGE:
                            currentItem++;
                            viewGroupHolder.adverboardviewPager.setCurrentItem(currentItem);
                            // 准备下次播放
                            viewGroupHolder.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                            break;
                        case MSG_KEEP_SILENT:
                            // 只要不发送消息就暂停了
                            break;
                        case MSG_BREAK_SILENT:
                            viewGroupHolder.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                            break;
                        case MSG_PAGE_CHANGED:
                            // 记录当前的页号，避免播放的时候页面显示不正确。
                            int position = msg.arg1;
                            int lastIndex = 0;
                            int index = position % viewGroupHolder.mImageList.size();
                            lastIndex = index;
                            currentItem = position;
                            break;
                        default:
                            break;
                    }
                }
            }

            /**
             * 根据传入数据展示直播列表
             * **/
            //TODO 为每个控件绑定事件
            public void showLivelist(View view, Context context, ShowData.LiveListData liveListData) {
                //直播间列表标题
                TextView textViewListtitle = view.findViewById(R.id.textViewListtitle);
                textViewListtitle.setText(liveListData.listtitle);
                Drawable icon;
                switch (liveListData.listtitle) {
                    case "全部直播":
                        icon = getResources().getDrawable(R.drawable.ic_livelist_alllive);
                        break;
                    case "绝地求生":
                        icon = getResources().getDrawable(R.drawable.ic_livelist_chick);
                        break;
                    case "刺激战场":
                        icon = getResources().getDrawable(R.drawable.ic_livelist_war);
                        break;
                    case "吃喝玩乐":
                        icon = getResources().getDrawable(R.drawable.ic_livelist_edph);
                        break;
                    default:
                        icon = getResources().getDrawable(R.drawable.ic_livelist_alllive);
                        break;
                }
                icon.setBounds(0, 0, 35, 35);
                textViewListtitle.setCompoundDrawables(icon, null, null, null);

                for (int i = 0; i < liveListData.size; i++) {
                    LinearLayout itembase;
                    switch (i) {
                        case 0:
                            itembase = view.findViewById(R.id.row0col0);
                            break;
                        case 1:
                            itembase = view.findViewById(R.id.row0col1);
                            break;
                        case 2:
                            itembase = view.findViewById(R.id.row1col0);
                            break;
                        case 3:
                            itembase = view.findViewById(R.id.row1col1);
                            break;
                        case 4:
                            itembase = view.findViewById(R.id.row2col0);
                            break;
                        case 5:
                            itembase = view.findViewById(R.id.row2col1);
                            break;
                        default:
                            return;
                    }
                    View item_live = itemInflater.inflate(R.layout.item_live, (ViewGroup) view, false);
                    //为整个布局设置点击响应

                    ImageView imageView = item_live.findViewById(R.id.imageView);
                    //开启线程下载url图片并显示
                    if (liveListData.urls[i] != "") {
                        //检查授权
                        int REQUEST_EXTERNAL_STORAGE = 1;
                        String[] PERMISSIONS_STORAGE = {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
                        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            // We don't have permission so prompt the user
                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    PERMISSIONS_STORAGE,
                                    REQUEST_EXTERNAL_STORAGE
                            );
                        }
                        HttpImgThread httpImgThread = new HttpImgThread(handler, imageView, liveListData.urls[i]);
                        new Thread(httpImgThread).start();
                    }

                    TextView textViewTitle = item_live.findViewById(R.id.textViewTitle);
                    TextView textViewTag = item_live.findViewById(R.id.textViewTag);
                    TextView textViewName = item_live.findViewById(R.id.textViewName);
                    TextView textViewAmount = item_live.findViewById(R.id.textViewAmount);
                    textViewTitle.setText(liveListData.titles[i]);
                    textViewTag.setText(liveListData.tabs[i]);
                    textViewName.setText(liveListData.name[i]);
                    textViewAmount.setText(liveListData.amount[i]);

                    itembase.addView(item_live);
                }
            }

            /**
             * 根据传入数据显示活动
             * **/
            //TODO 为每个活动绑定事件
            ActivityHandler activityHandler = new ActivityHandler(new WeakReference<ViewGroupHolder>(this));
            VerticalViewPager activityviewPager;
            ShowData.ActivityData activityData;
            List<View> viewlist = new ArrayList<>();
            public void showActivity(View view, Context context, ShowData.ActivityData activityData) {
                this.activityData = activityData;
                Button activitybt = view.findViewById(R.id.activityBt);//点击进入活动页面
                activityviewPager = view.findViewById(R.id.activityViewPager);
                for (int i = 0; i < activityData.titles.length; i++) {
                    View activityitem = itemInflater.inflate(R.layout.item_activity_item, parentview, false);
                    Button orderbt = activityitem.findViewById(R.id.orderbt);
                    TextView titleText = activityitem.findViewById(R.id.titleText);
                    TextView activityText = activityitem.findViewById(R.id.activityText);
                    if(activityData.hasOrder[i]==false){
                        orderbt.setVisibility(View.GONE);
                    }
                    titleText.setText(activityData.titles[i]);
                    activityText.setText(activityData.text[i]);
                    viewlist.add(activityitem);
                }
                activityviewPager.setAdapter(new ActivityAdapter(viewlist));
                activityviewPager.setCurrentItem(0);
                activityviewPager.setFocusable(true);
                activityviewPager.setOnTouchListener(this);
                activityHandler.sendEmptyMessageDelayed(AdverboardHandler.MSG_UPDATE_IMAGE, AdverboardHandler.MSG_DELAY);
            }

            //活动的 ViewPager适配器
            private class ActivityAdapter extends PagerAdapter {
                private List<View> viewlist;

                public ActivityAdapter(List<View> viewlist) {
                    this.viewlist = viewlist;
                }

                @Override
                public int getCount() {
                    return viewlist.size();
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    // Warning：不要在这里调用removeView
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    // 对ViewPager页号求模取出View列表中要显示的项
                    position %= viewlist.size();
                    if (position < 0) {
                        position = viewlist.size() + position;
                    }
                    View view = viewlist.get(position);
                    // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                    ViewParent vp = view.getParent();
                    if (vp != null) {
                        ViewGroup parent = (ViewGroup) vp;
                        parent.removeView(view);
                    }
                    container.addView(view);
                    // 此处可添加监听事件

                    return view;
                }
            }

            //活动的对应Handler 处理自动滚动的功能
            private class ActivityHandler extends Handler {
                /**
                 * 请求更新显示的View。
                 */
                protected static final int MSG_UPDATE_IMAGE = 1;
//                /**
//                 * 请求暂停轮播。
//                 */
//                protected static final int MSG_KEEP_SILENT = 2;
//                /**
//                 * 请求恢复轮播。
//                 */
//                protected static final int MSG_BREAK_SILENT = 3;
//                /**
//                 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
//                 * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
//                 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
//                 */
//                protected static final int MSG_PAGE_CHANGED = 4;
                // 轮播间隔时间
                protected static final long MSG_DELAY = 3000;
                //  使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
                private WeakReference<ViewGroupHolder> weakReference;
                private int currentItem = 0;

                protected ActivityHandler(WeakReference<ViewGroupHolder> wk) {
                    weakReference = wk;
                }

                @Override
                //TODO
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    final ViewGroupHolder viewGroupHolder = weakReference.get();
                    if (viewGroupHolder == null) {
                        // viewGroupHolder 已经回收，无需再处理UI了
                        return;
                    }
                    // 第一次不删重复的消息
                    if (currentItem != 0) {
                        // 检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
                        if (viewGroupHolder.activityHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                            viewGroupHolder.activityHandler.removeMessages(MSG_UPDATE_IMAGE);
                        }
                    }
                    switch (msg.what) {
                        case MSG_UPDATE_IMAGE:
                            currentItem++;
                            currentItem = currentItem%viewlist.size();
                            if(currentItem < 0)
                                currentItem += viewlist.size();
                            viewGroupHolder.activityviewPager.setCurrentItem(currentItem);
                            // 准备下次播放
                            viewGroupHolder.activityHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                            break;
//                        case MSG_KEEP_SILENT:
//                            // 只要不发送消息就暂停了
//                            break;
//                        case MSG_BREAK_SILENT:
//                            viewGroupHolder.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                            break;
//                        case MSG_PAGE_CHANGED:
//                            // 记录当前的页号，避免播放的时候页面显示不正确。
//                            int position = msg.arg1;
//                            int lastIndex = 0;
//                            int index = position % viewGroupHolder.mImageList.size();
//                            lastIndex = index;
//                            currentItem = position;
//                            break;
                        default:
                            break;
                    }
                }
            }

            /**
             * 根据传入数据显示广告列表
             * **/
            public void showAdverlist(View view, Context context, ShowData.AdverlistData adverlistData){
                TextView listtitle = view.findViewById(R.id.listtitle);
                listtitle.setText(adverlistData.listtitle);
                Drawable icon;
                switch (adverlistData.listtitle) {
                    case "最新热游":
                        icon = getResources().getDrawable(R.drawable.ic_adverlist);
                        break;
                    default:
                        icon = getResources().getDrawable(R.drawable.ic_adverlist);
                        break;
                }
                icon.setBounds(0, 0, 40, 40);
                listtitle.setCompoundDrawables(icon, null, null, null);

                LinearLayout linerAverlist = view.findViewById(R.id.linerAverlist);
                for(int i = 0;i<adverlistData.advertitle.length; i++) {
                    View itemview = itemInflater.inflate(R.layout.item_adverlist_item, parentview, false);
                    TextView adverlistTitle = itemview.findViewById(R.id.adverlistTitle);
                    TextView adverlistText = itemview.findViewById(R.id.adverlistText);
                    ImageView itemIcon = itemview.findViewById(R.id.itemIcon);
                    adverlistTitle.setText(adverlistData.advertitle[i]);
                    adverlistText.setText(adverlistData.advertext[i]);

                    //itemIcon.setBackground();

                    linerAverlist.addView(itemview);
                }
            }
        }
    }
}

