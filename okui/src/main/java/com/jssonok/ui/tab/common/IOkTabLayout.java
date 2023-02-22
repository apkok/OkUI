package com.jssonok.ui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 *
 * @param <Tab> 泛型（既可以是底部导航的Tab，也可以是顶部导航的Tab）
 * @param <D> 泛型（代表Tab对应的数据）
 */
public interface IOkTabLayout<Tab extends ViewGroup, D> {

    // 根据数据查找对应的Tab
    Tab findTab(@NonNull D data);

    // 添加Tab选择切换的监听器
    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    // 设置默认选择的Tab
    void defaultSelected(@NonNull D defaultInfo);

    // 初始化数据
    void inflateInfo(@NonNull List<D> infoList);

    /**
     * 设置Tab选择监听接口（当底部Tab被选择的时候，通知对应的监听器，反馈Tab的位置索引、上一个选中的Tab的数据以及下一个选中的Tab的数据）
     * @param <D>
     */
    interface OnTabSelectedListener<D> {
        void onTabSelectedChange(int index, @Nullable D prevInfo, @NonNull D nextInfo);
    }
}
