package com.example.crow.huyademo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.crow.huyademo.R;


public class CategoryTabStrip extends HorizontalScrollView {
    private LayoutInflater mLayoutInflater;
    private final PageListener pageListener = new PageListener();
    private ViewPager pager;
    private LinearLayout tabsContainer;
    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Rect indicatorRect;

    private LinearLayout.LayoutParams defaultTabLayoutParams;

    private int scrollOffset = 10;
    private int lastScrollX = 0;

    private Drawable indicator;

    private TextDrawable[] drawables;
//    private Drawable left_edge;
    private Drawable right_edge;

    //EdgeEffect

    public CategoryTabStrip(Context context) {
        this(context, null);
    }

    public CategoryTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mLayoutInflater = LayoutInflater.from(context);
        drawables = new TextDrawable[3];

        indicatorRect = new Rect();

        setFillViewport(true);
        setWillNotDraw(false);

        // 标签容器
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);


        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // 绘制高亮区域作为滑动分页指示器
        indicator = getResources().getDrawable(R.drawable.bg_category_indicator);
        // 右边界阴影效果
        //left_edge = getResources().getDrawable(R.drawable.ic_category_left_edge);
        right_edge = getResources().getDrawable(R.drawable.ic_category_right_edge);
    }

    // 绑定与CategoryTabStrip控件对应的ViewPager控件，实现联动
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    // 当附加在ViewPager适配器上的数据发生变化时,应该调用该方法通知CategoryTabStrip刷新数据
    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            addTab(i, pager.getAdapter().getPageTitle(i).toString());
        }
    }

    // 添加一个标签到导航菜单
    private void addTab(final int position, String title) {
        ViewGroup tab = (ViewGroup)mLayoutInflater.inflate(R.layout.category_tab, this, false);
        TextView category_text = (TextView) tab.findViewById(R.id.category_text);
        category_text.setText(title);
        //字体加粗
        TextPaint paint = category_text.getPaint();
        paint.setFakeBoldText(true);
        category_text.setSingleLine();
        category_text.setFocusable(true);
        category_text.setTextColor(getResources().getColor(R.color.category_tab_text));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //currentPosition = position;
                //calculateIndicatorRect(indicatorRect);
                pager.setCurrentItem(position);
                invalidate();
            }
        });
        tabsContainer.addView(tab, position, defaultTabLayoutParams);
    }





    // 计算滑动过程中矩形高亮区域的上下左右位置
    private void calculateIndicatorRect(Rect rect) {
        ViewGroup currentTab = (ViewGroup)tabsContainer.getChildAt(currentPosition);
        TextView category_text = (TextView) currentTab.findViewById(R.id.category_text);

        float left = (float) (currentTab.getLeft() + category_text.getLeft());
        float width = ((float) category_text.getWidth()) + left;

        //不能自动调整到对应的位置
//        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
//            ViewGroup nextTab = (ViewGroup)tabsContainer.getChildAt(currentPosition + 1);
//            TextView next_category_text = (TextView) nextTab.findViewById(R.id.category_text);
//
//            float next_left = (float) (nextTab.getLeft() + next_category_text.getLeft());
//            left = left * (1.0f - currentPositionOffset) + next_left * currentPositionOffset;
//            width = width * (1.0f - currentPositionOffset) + currentPositionOffset * (((float) next_category_text.getWidth()) + next_left);
//        }
        int expand = 15;//字体变大，对应矩形也变大
        rect.set(((int) left) + getPaddingLeft()-expand, getPaddingTop() + currentTab.getTop() + category_text.getTop()-expand,
                ((int) width) + getPaddingLeft()+expand, currentTab.getTop() + getPaddingTop() + category_text.getTop() + category_text.getHeight()+expand);
    }

    // 计算滚动范围
    private int getScrollRange() {
        return getChildCount() > 0 ? Math.max(0, getChildAt(0).getWidth() - getWidth() + getPaddingLeft() + getPaddingRight()) : 0;
    }

    // CategoryTabStrip与ViewPager联动逻辑
    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        calculateIndicatorRect(indicatorRect);
        int newScrollX = lastScrollX;
        if (indicatorRect.left < getScrollX() + scrollOffset) {
            newScrollX = indicatorRect.left - scrollOffset;
        } else if (indicatorRect.right > getScrollX() + getWidth() - scrollOffset) {
            newScrollX = indicatorRect.right - getWidth() + scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    // 自定义绘图
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // 绘制高亮背景矩形红框
        calculateIndicatorRect(indicatorRect);
        if(indicator != null) {
            indicator.setBounds(indicatorRect);
            indicator.draw(canvas);
        }

        // 绘制背景红框内标签文本
        int i = 0;
        while (i < tabsContainer.getChildCount()) {
            if (i < currentPosition - 1 || i > currentPosition + 1) {
                i++;
            } else {//被选择项绘制
                ViewGroup tab = (ViewGroup)tabsContainer.getChildAt(i);
                TextView category_text = (TextView) tab.findViewById(R.id.category_text);
                if (category_text != null) {
                    int expand = 10;//被选择时字体变大
                    drawables[i - currentPosition + 1] = TextDrawable.builder()
                            .beginConfig()
                                .textColor(Color.YELLOW)
                                .fontSize((int)category_text.getTextSize()+ expand)
                            .endConfig()
                            .buildRect(category_text.getText().toString(),getResources().getColor(R.color.colorPrimary));
                    TextDrawable textDrawable = drawables[i - currentPosition + 1];
                    int save = canvas.save();
                    calculateIndicatorRect(indicatorRect);
                    canvas.clipRect(indicatorRect);
                    int left = tab.getLeft() + category_text.getLeft() + (category_text.getWidth() - textDrawable.getIntrinsicWidth()) / 2 + getPaddingLeft();
                    int top = tab.getTop() + category_text.getTop() + (category_text.getHeight() - textDrawable.getIntrinsicHeight()) / 2 + getPaddingTop();
                    textDrawable.setBounds(left, top, textDrawable.getIntrinsicWidth() + left, textDrawable.getIntrinsicHeight() + top);
                    textDrawable.draw(canvas);
                    canvas.restoreToCount(save);
                }
                i++;
            }
        }
        // 绘制左右边界阴影效果
        i = canvas.save();
        int top = getScrollX();
        int height = getHeight();
        int width = getWidth();
        canvas.translate((float) top, 0.0f);
//        if (left_edge == null || top <= 0) {
//            if (right_edge == null || top >= getScrollRange()) {
//                canvas.restoreToCount(i);
//            }
//            right_edge.setBounds(width - right_edge.getIntrinsicWidth(), 0, width, height);
//            right_edge.draw(canvas);
//            canvas.restoreToCount(i);
//        }
//        left_edge.setBounds(0, 0, left_edge.getIntrinsicWidth(), height);
//        left_edge.draw(canvas);
        if (right_edge == null || top >= getScrollRange()) {
            canvas.restoreToCount(i);
        }
        right_edge.setBounds(width - right_edge.getIntrinsicWidth(), 0, width, height);
        right_edge.draw(canvas);
        canvas.restoreToCount(i);
    }

    private class PageListener implements ViewPager.OnPageChangeListener {
        //当点击选项与原来选项相隔一个以上 此项置为true
        boolean flag = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(flag == false)//当flag为true currentPosition使用onPageSelected传入的值
                currentPosition = position;
            currentPositionOffset = positionOffset;
//            Log.i("Temp","Pos Scroll:"+position);
            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.i("onPageScrollStateChange",""+state);
            //Log.i("onPageScrollStateChange","Item:"+pager.getCurrentItem());
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if(pager.getCurrentItem() == 0) {
                    // 滑动到最左边
                    scrollTo(0, 0);
                } else if (pager.getCurrentItem() == tabCount - 1) {
                    // 滑动到最右边
                    scrollTo(getScrollRange(), 0);
                } else {
                    scrollToChild(pager.getCurrentItem(), 0);
                }
            }
            else if(state == ViewPager.SCROLL_STATE_DRAGGING){

            }
            else if(state == ViewPager.SCROLL_STATE_SETTLING){

            }
        }

        @Override
        public void onPageSelected(int position) {
            //Log.i("Temp","Pos select:"+position);
            if(Math.abs(position-currentPosition)<=1) {
                //Log.i("Temp", "Pos select:" + position);
                flag = false;
            }
            else{
                currentPosition = position;
                flag = true;
            }
            invalidate();
        }
    }
}
