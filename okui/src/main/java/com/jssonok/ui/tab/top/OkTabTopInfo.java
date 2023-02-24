package com.jssonok.ui.tab.top;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * 顶部导航栏实体类
 * @param <Color> 支持Color泛型（为了Color可扩展，Color既可以设置int类型的，也可以设置String类型的）
 */
public class OkTabTopInfo<Color> {
    /**
     * Tab类型（内部枚举类，支持Bitmap、TEXT）
     */
    public enum TabType {
        BITMAP, TEXT
    }

    // 拥有Fragment的class即可动态创建Fragment实例
    public Class<? extends Fragment> fragment;
    // Tab名称
    public String name;
    // Tab未选中状态下的图标
    public Bitmap defaultBitmap;
    // Tab选中状态下的图标
    public Bitmap selectedBitmap;
    // Tab未选中状态下的颜色
    public Color defaultColor;
    // Tab选中状态下的颜色
    public Color tintColor;
    // Tab类型
    public TabType tabType;

    public OkTabTopInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public OkTabTopInfo(String name, Color defaultColor, Color tintColor) {
        this.name = name;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.TEXT;
    }
}
