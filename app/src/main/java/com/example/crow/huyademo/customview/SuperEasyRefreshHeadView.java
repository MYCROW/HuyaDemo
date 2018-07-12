package com.example.crow.huyademo.customview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.crow.huyademo.R;

/**
 *
 * 下拉刷新的头部view，这个view可以任意修改样式
 */
public class SuperEasyRefreshHeadView extends LinearLayout {

    private ProgressBar progressBar;
    public int headViewHeight;
    public  TextView textView;

    SuperEasyRefreshHeadView(Context context) {
        super(context);
        View view = View.inflate(getContext(), R.layout.view_super_easy_refresh_head, null);
        textView = (TextView) view.findViewById(R.id.super_easy_refresh_text_view);
        progressBar = (ProgressBar) view.findViewById(R.id.super_easy_refresh_head_progress_bar);
        addView(view);
        hideProgressBar();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();

        headViewHeight = (int) (40 * metrics.density);//注意高度的设置。
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(headViewHeight, MeasureSpec.EXACTLY));
    }

    /**
     * 设置刷新的文本
     * */
    public void setRefreshText(String text){
        textView.setText(text);
    }
    /**
     * 隐藏ProgressBar
     * */
    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 显示ProgressBar
     * */
    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }


}