package com.example.crow.huyademo.fragment;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.crow.huyademo.MainActivity;
import com.example.crow.huyademo.R;
import com.example.crow.huyademo.thread.HttpImgThread;

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
        args.putString(NUM, number+"");
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
        if(num == 0) { //推荐
            view = inflater.inflate(R.layout.fragment_index0, container, false);
            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listview
            mRecyclerView.setAdapter(new RecyclerViewAdapter(context));
        }
        else {
            view = inflater.inflate(R.layout.fragment_index1, container, false);
            TextView tx = view.findViewById(R.id.test1);
            tx.setText(""+num);
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
            int datatype;
            boolean hasData;

            public ShowData(int type) {
                if (type != SEARCH &&
                        type != LIVE &&
                        type != ADBORAD &&
                        type != ACTIVI &&
                        type != ADLIST)
                    datatype = DEFAULTTYPE;
                else
                    datatype = type;
                hasData = false;
            }

            //Search data 搜索框数据
            SearchData searchData;
            public class SearchData {
                public String bgtext;//搜索框背景文字
                public boolean hasScanBtn;//是否有扫描二维码按钮
            }
            void addDataSearch(String bgtext,boolean hasScanBtn){
                if(datatype != SEARCH || hasData == true)
                    return;
                searchData = new SearchData();
                searchData.bgtext = bgtext;
                searchData.hasScanBtn = hasScanBtn;
                hasData = true;
            }
            //Live list data直播列表数据
            LiveListData liveListData;
            public class LiveListData{
                public String listtitle;
                public String [] titles;//标题
                public String [] urls;//预览图
                public String [] liveurl;
                public String [] tabs;//直播间标签
                public String [] name;//直播间名
                public String [] amount;//直播间人数
                public int size;//列表尺寸
            }
            void addDataLivelist(String listtitle, String []titles, String []urls, String []liveurl, String []tabs, String []name, String []amount, int size){
                if(datatype != LIVE || hasData == true)
                    return;
                liveListData = new LiveListData();
                liveListData.listtitle = listtitle;
                liveListData.titles = titles;
                liveListData.urls = urls;
                liveListData.liveurl = liveurl;
                liveListData.tabs = tabs;
                liveListData.name = name;
                liveListData.amount = amount;
                if(size<=6)
                    liveListData.size = size;
                else
                    liveListData.size = 6;
                hasData = true;
            }

            //Advertisment board 广告板数据
            AdverboardData adverData;
            public class AdverboardData{
                public String []imageUrl;
                public String []targetUrl;
                public int []index;
                public String []tag;
            }
            void addDataAdver(String []imageUrl,String []targetUrl,int []index,String []tag){
                if(datatype != ADBORAD || hasData == true)
                    return;
                adverData = new AdverboardData();
                adverData.imageUrl = imageUrl;
                adverData.targetUrl = targetUrl;
                adverData.index = index;
                adverData.tag = tag;
                hasData = true;
            }

            public int getType(){
                return datatype;
            }

            public Object getData(){
                if(hasData == false)
                    return null;
                if(datatype == SEARCH)
                    return searchData;
                if(datatype == LIVE)
                    return liveListData;
                if(datatype == ADBORAD) {
                    return adverData;
                }
                return null;
            }

        }

        public RecyclerViewAdapter(Context context) {
            //mTitles = context.getResources().getStringArray(R.array.titles);
            data = new ArrayList<>();
            //添加搜索框项
            ShowData itemdata0 = new ShowData(SEARCH);
            itemdata0.addDataSearch("text",true);
            data.add(0,itemdata0);
            //添加广告版项
            ShowData itemdata1 = new ShowData(ADBORAD);
            String []imageUrl = {"1","1","1","1"};
            String []targetUrl = {"1","1","1","1"};
            int []index = {0, 1, 2, 3};
            String []tag = {"1", "1", "1", "1"};
            itemdata1.addDataAdver(imageUrl, targetUrl, index, tag);
            data.add(1,itemdata1);
            //添加直播列表项
            ShowData itemdata2 = new ShowData(LIVE);
            String listtitle = "全部直播";
            String []titles = {"标题1","标题2","标题3","标题4","标题5","标题6"};
            String []imageUrl2 = {"http://img.sccnn.com/bimg/338/50000.jpg",
                    "https://p9.pstatp.com/weili/l/79053546024309963.webp",
                    "http://pic1.ooopic.com/uploadfilepic/sheying/2009-10-29/OOOPIC_wcl00124_2009102992c2017a3e978ef7.jpg","","",""};
            String []liveUrl = {"","","","","",""};
            String []livetag = {"绝地求生","刺激战场","暴雪专区","LOL","绝地求生","绝地求生"};
            String []livename = {"二丫","DGTY","Nova","青蛙","韦神","二大爷"};
            String []amount = {"1.1万","5万","4500","11万","902","4111"};
            int size = 6;
            itemdata2.addDataLivelist(listtitle, titles, imageUrl2, liveUrl, livetag, livename, amount, size);
            data.add(2,itemdata2);

            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewGroupHolder(mLayoutInflater.inflate(R.layout.item_group, parent, false),context);
        }

        @Override
        public void onBindViewHolder(ViewGroupHolder holder, int position) {
            holder.show(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        public class ViewGroupHolder extends RecyclerView.ViewHolder {
            ViewGroup parentview;
            View itemview;
            private final LayoutInflater itemInflater;
            Handler handler;

            ViewGroupHolder(View view,Context context) {
                super(view);
                parentview = (ViewGroup)view;
                itemInflater = LayoutInflater.from(context);
                handler = new Handler();
            }

            public void show(ShowData data){
                switch (data.getType()){
                    case SEARCH:
                        itemview = itemInflater.inflate(R.layout.item_search,parentview,false);
                        SearchView searchView = itemview.findViewById(R.id.searchview);
                        Button scanbutton = itemview.findViewById(R.id.scanbt);
                        ShowData.SearchData searchData = (ShowData.SearchData)data.getData();
                        //扫描按钮
                        if(searchData.hasScanBtn == false) {
                            scanbutton.setVisibility(View.GONE);
                        }
                        else{
                            scanbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //调用摄像头
                                }
                            });
                        }
                        //搜索框
                        searchView.setQueryHint("这里其实应该是按钮:"+searchData.bgtext);
                        break;
                    case LIVE:
                        itemview = itemInflater.inflate(R.layout.item_livelist,parentview,false);
                        ShowData.LiveListData liveData = (ShowData.LiveListData)data.getData();
                        showLivelist(itemview,context, liveData);
                        break;
                    case ADBORAD:
                        itemview = itemInflater.inflate(R.layout.item_adverboard,parentview,false);
                        ShowData.AdverboardData adboardData = (ShowData.AdverboardData)data.getData();
                        showAdverboard(itemview,context,adboardData);
                        break;
                }
                parentview.addView(itemview);
            }

            //根据传入数据展示广告版
            /** TODO **/
            public void showAdverboard(View view,Context context,ShowData.AdverboardData adboardData){
                String []imageUrl = adboardData.imageUrl;
                String []targetUrl = adboardData.targetUrl;
                int []index = adboardData.index;
                String []tag = adboardData.tag;

//                ViewPager adverpager = view.findViewById(R.id.adverpager);
//                TextView tagtext = view.findViewById(R.id.advtext);
//                RadioGroup radiogroup = view.findViewById(R.id.radiogroup);
//                PagerAdapter pAdapter;
//                Runnable autoRoll;
//                Handler handler = new Handler();
//
//                List<ImageView> list_image = new ArrayList<>();
            }


            //根据传入数据展示直播列表
            //TODO
            public void showLivelist(View view, Context context, ShowData.LiveListData liveListData){
                //直播间列表标题
                TextView textViewListtitle = view.findViewById(R.id.textViewListtitle);
                textViewListtitle.setText(liveListData.listtitle);
                Drawable icon;
                switch(liveListData.listtitle){
                    case "全部直播":icon = getResources().getDrawable(R.drawable.ic_livelist_alllive);break;
                    case "绝地求生":icon = getResources().getDrawable(R.drawable.ic_livelist_chick);break;
                    case "刺激战场":icon = getResources().getDrawable(R.drawable.ic_livelist_war);break;
                    case "吃喝玩乐":icon = getResources().getDrawable(R.drawable.ic_livelist_edph);break;
                    default:icon = getResources().getDrawable(R.drawable.ic_livelist_alllive);break;
                }
                icon.setBounds(0,0,35,35);
                textViewListtitle.setCompoundDrawables(icon,null,null,null);

                for(int i = 0;i<liveListData.size;i++){
                    LinearLayout itembase;
                    switch (i){
                        case 0:itembase = view.findViewById(R.id.row0col0);break;
                        case 1:itembase = view.findViewById(R.id.row0col1);break;
                        case 2:itembase = view.findViewById(R.id.row1col0);break;
                        case 3:itembase = view.findViewById(R.id.row1col1);break;
                        case 4:itembase = view.findViewById(R.id.row2col0);break;
                        case 5:itembase = view.findViewById(R.id.row2col1);break;
                        default:return;
                    }
                    View item_live = itemInflater.inflate(R.layout.item_live,(ViewGroup) view,false);
                    //为整个布局设置点击响应

                    ImageView imageView= item_live.findViewById(R.id.imageView);
                    //开启线程下载url图片并显示
                    if(liveListData.urls[i] != ""){
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
                        HttpImgThread httpImgThread = new HttpImgThread(handler,imageView,liveListData.urls[i]);
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


        }
    }


}
