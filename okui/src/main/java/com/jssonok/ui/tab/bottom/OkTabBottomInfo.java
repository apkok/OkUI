package com.jssonok.ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * 底部导航栏实体类
 * @param <Color> 支持Color泛型（为了Color可扩展，Color既可以设置int类型的，也可以设置String类型的）
 */
public class OkTabBottomInfo<Color> {
    /**
     * Tab类型（内部枚举类，支持Bitmap、icon）
     */
    public enum TabType {
        BITMAP, ICON
    }

    // 拥有Fragment的class即可动态创建Fragment实例
    public Class<? extends Fragment> fragment;
    // TabBottom名称
    public String name;
    // TabBottom未选中状态下的图标
    public Bitmap defaultBitmap;
    // TabBottom选中状态下的图标
    public Bitmap selectedBitmap;
    /**
     * Tips：在Java代码中直接设置iconFont字符串无效，需要定义在string.xml
     */
    // iconFont字体
    public String iconFont;
    // TabBottom未选中状态下的iconFont字符串的名称
    public String defaultIconName;
    // TabBottom选中状态下的iconFont字符串的名称
    public String selectedIconName;
    // TabBottom未选中状态下的颜色
    public Color defaultColor;
    // TabBottom选中状态下的颜色
    public Color tintColor;
    // Tab类型
    public TabType tabType;

    public OkTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public OkTabBottomInfo(String name, String iconFont, String defaultIconName, String selectedIconName, Color defaultColor, Color tintColor) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.ICON;
    }
}
