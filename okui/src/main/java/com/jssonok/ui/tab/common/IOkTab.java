package com.jssonok.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface IOkTab<D> extends IOkTabLayout.OnTabSelectedListener<D> {

    /**
     * 动态设置Tab的数据
     * @param data 数据
     */
    void setOkTabInfo(@NonNull D data);

    /**
     * 动态修改某个item的高度
     * @param height 高度
     */
    void resetHeight(@Px int height);
}

