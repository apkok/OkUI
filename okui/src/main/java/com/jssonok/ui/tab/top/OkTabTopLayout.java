package com.jssonok.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.jssonok.library.util.OkDisplayUtil;
import com.jssonok.ui.tab.common.IOkTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OkTabTopLayout extends HorizontalScrollView implements IOkTabLayout<OkTabTop, OkTabTopInfo<?>> {

    private List<OnTabSelectedListener<OkTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private OkTabTopInfo<?> selectedInfo;
    private List<OkTabTopInfo<?>> infoList;
    private int tabWidth;

    public OkTabTopLayout(Context context) {
        this(context, null);
    }

    public OkTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OkTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setVerticalScrollBarEnabled(false);
    }


    @Override
    public OkTabTop findTab(@NonNull OkTabTopInfo<?> info) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof OkTabTop) {
                OkTabTop tab = (OkTabTop) child;
                if (tab.getOkTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<OkTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull OkTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<OkTabTopInfo<?>> infoList) {
        if(infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        LinearLayout linearLayout = getRootLayout(true);
        selectedInfo = null;
        // 清除之前添加的OkTabTop listener，Tips：Java foreach remove问题
        Iterator<OnTabSelectedListener<OkTabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if(iterator.next() instanceof OkTabTop) {
                iterator.remove();
            }
        }
        for (int i = 0; i < infoList.size(); i++) {
            OkTabTopInfo<?> info = infoList.get(i);
            OkTabTop tab = new OkTabTop(getContext());
            tabSelectedChangeListeners.add(tab);
            tab.setOkTabInfo(info);
            linearLayout.addView(tab);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(info);
                }
            });
        }
    }

    private void onSelected(@NonNull OkTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<OkTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;

        autoScroll(nextInfo);
    }

    /**
     * 自动滚动，实现点击的位置能够自动滚动以展示前后2个
     * @param nextInfo 点击Tab的info
     */
    private void autoScroll(OkTabTopInfo<?> nextInfo) {
        OkTabTop tabTop = findTab(nextInfo);
        if (tabTop == null) return;
        int index = infoList.indexOf(nextInfo);
        int[] location = new int[2];
        // 获取点击的控件在屏幕的位置
        tabTop.getLocationInWindow(location);
        // 需要滚动的距离
        int scrollWidth;
        if (tabWidth == 0) {
            tabWidth = tabTop.getWidth();
        }
        // 判断点击了屏幕左侧还是右侧
        if((location[0] + tabWidth / 2) > OkDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            // 向右侧查找2个控件
            scrollWidth = rangeScrollWidth(index, 2);
        }else {
            // 向左侧查找2个控件
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth, 0);
    }

    /**
     * 获取可滚动的范围
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i <= Math.abs(range); i++) {
            int next;
            if(range < 0) {
                next = range + i + index;
            }else {
                next = range - i + index;
            }
            if (next >= 0 && next < infoList.size()) {
                if(range < 0) {
                    // 点击屏幕左侧
                    scrollWidth -= scrollWidth(next, false);
                }else {
                    // 点击屏幕右侧
                    scrollWidth += scrollWidth(next, true);
                }
            }
        }
        return scrollWidth;
    }

    /**
     * 指定位置的控件可滚动的距离
     * @param index 指定位置的控件
     * @param toRight 是否是点击了屏幕右侧
     * @return 可滚动的距离
     */
    private int scrollWidth(int index, boolean toRight) {
        OkTabTop target = findTab(infoList.get(index));
        if(target == null) return 0;
        Rect rect = new Rect();
        // 通过该方法判断视图是否可见
        target.getLocalVisibleRect(rect);
        if(toRight) {
            // 点击屏幕右侧
            if(rect.right > tabWidth) {
                // right坐标大于控件的宽度时，说明完全没有显示
                return tabWidth;
            }else {
                // 显示部分，减去已显示的宽度
                return tabWidth - rect.right;
            }
        }else {
            // 点击屏幕左侧
            if(rect.left <= -tabWidth) {
                // left坐标小于等于-控件的宽度时，说明完全没有显示
                return tabWidth;
            }else if (rect.left > 0){
                // 显示部分
                return rect.left;
            }
            return 0;
        }
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addView(rootView, params);
        }else if(clear) {
            rootView.removeAllViews();
        }

        return rootView;
    }
}
