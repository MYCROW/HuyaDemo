package com.example.crow.huyademo.customview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crow.huyademo.R;

/**
 *
 * 加载更多的view，这个view的样式可以任意修改。
 */
public class SuperEasyRefreshFootView extends LinearLayout {

    public int footViewHeight;
    public  TextView textView;

    SuperEasyRefreshFootView(Context context) {
        super(context);
        View view = View.inflate(getContext(), R.layout.view_super_easy_refresh_foot, null);
        textView = (TextView) view.findViewById(R.id.super_easy_refresh_text_view);
        addView(view);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        footViewHeight = (int) (50 * metrics.density);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(footViewHeight, MeasureSpec.EXACTLY));
    }

}