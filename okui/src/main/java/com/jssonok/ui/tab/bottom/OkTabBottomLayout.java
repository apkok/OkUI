package com.jssonok.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jssonok.library.util.OkDisplayUtil;
import com.jssonok.library.util.OkViewUtil;
import com.jssonok.ui.R;
import com.jssonok.ui.tab.common.IOkTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OkTabBottomLayout extends FrameLayout implements IOkTabLayout<OkTabBottom, OkTabBottomInfo<?>> {
    // 存放所有TabBottom监听事件
    private List<OnTabSelectedListener<OkTabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    // 保存当前选中的TabBottom对应的数据
    private OkTabBottomInfo<?> selectedInfo;
    // 透明度（1f为不透明）
    private float bottomAlpha = 1f;
    // TabBottom高度
    private float tabBottomHeight = 50;
    // TabBottom的头部线条高度
    private float bottomLineHeight = 0.5f;
    // TabBottom的头部线条颜色
    private String bottomLineColor = "#dfe0e1";
    // TabBottom对应的数据集合
    private List<OkTabBottomInfo<?>> infoList;

    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";

    public OkTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public OkTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OkTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public OkTabBottom findTab(@NonNull OkTabBottomInfo<?> data) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if(child instanceof OkTabBottom) {
                OkTabBottom tab = (OkTabBottom) child;
                if(tab.getOkTabBottomInfo() == data) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<OkTabBottomInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull OkTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<OkTabBottomInfo<?>> infoList) {
        if(infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        // 清除之前添加的OkTabBottom listener，Tips：Java foreach remove问题
        // 两种解决方案：1、for循环，从后往前删除；2、使用Iterator迭代器
        Iterator<OnTabSelectedListener<OkTabBottomInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if(iterator.next() instanceof OkTabBottom) {
                iterator.remove();
            }
        }
        // 添加底部TabBottom
        // 创建FrameLayout，将TabBottom放在FrameLayout容器里面
        FrameLayout ll = new FrameLayout(getContext());
        ll.setTag(TAG_TAB_BOTTOM);
        int height = OkDisplayUtil.dp2px(tabBottomHeight, getResources());
        // 因为是FrameLayout容器，所以需要手动控制每个TabBottom的宽度（width）和左边距（leftMargin）
        // 那么为什么不使用LinearLayout容器呢？因为需要动态修改TabBottom的高度，如果使用LinearLayout容器的情况下，设置gravity会失效
        // Tips：为何不用LinearLayout？当动态改变child大小后Gravity.BOTTOM会失效
        int width = OkDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        for(int i = 0; i < infoList.size(); i++) {
            OkTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            // 控制TabBottom从左到右一次排列
            params.leftMargin = i * width;

            // 实例化OkTabBottom
            OkTabBottom tabBottom = new OkTabBottom(getContext());
            tabSelectedChangeListeners.add(tabBottom);
            // 给TabBottom设置对应的数据
            tabBottom.setOkTabInfo(info);
            // 将TabBottom添加至FrameLayout容器中
            ll.addView(tabBottom, params);
            // 为TabBottom设置点击事件
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(info);
                }
            });
        }

        LayoutParams flParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(ll, flParams);

        // 修复内容区域的底部Padding
        fixContentView();
    }

    /**
     * 添加底部TabBottom线条
     */
    private void addBottomLine() {
        // 创建View
        View bottomLine = new View(getContext());
        // 设置背景颜色
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));
        // 设置布局参数
        LayoutParams bottomLineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, OkDisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        // 设置距离底部的距离
        bottomLineParams.bottomMargin = OkDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(bottomAlpha);
    }

    /**
     * TabBottom选中的方法
     * @param nextInfo
     */
    private void onSelected(@NonNull OkTabBottomInfo<?> nextInfo) {
        // 通知所有的监听器，告知用户选择了某个TabBottom
        for (OnTabSelectedListener<OkTabBottomInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    /**
     * 添加背景及背景透明度
     */
    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ok_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, OkDisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    /**
     * 修复内容区域的底部Padding
     */
    private void fixContentView() {
        if(!(getChildAt(0) instanceof ViewGroup)) {
            // 单节点情况下，直接返回
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        // 查找RecyclerView
        ViewGroup targetView = OkViewUtil.findTypeView(rootView, RecyclerView.class);
        if(targetView == null) {
            // 查找ScrollView
            targetView = OkViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if(targetView == null) {
            // 查找AbsListView
            targetView = OkViewUtil.findTypeView(rootView, AbsListView.class);
        }

        if (targetView != null) {
            targetView.setPadding(0, 0, 0, OkDisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }

    public void setTabBottomAlpha(float alpha) {
        this.bottomAlpha = alpha;
    }

    public void setTabBottomHeight(float tabBottomHeight) {
        this.tabBottomHeight = tabBottomHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }
}
