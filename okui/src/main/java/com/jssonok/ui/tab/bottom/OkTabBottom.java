package com.jssonok.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jssonok.ui.R;
import com.jssonok.ui.tab.common.IOkTab;

public class OkTabBottom extends RelativeLayout implements IOkTab<OkTabBottomInfo<?>> {

    private OkTabBottomInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabIconView;
    private TextView tabNameView;

    public OkTabBottom(Context context) {
        this(context, null);
    }

    public OkTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OkTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 加载视图
     */
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ok_tab_bottom, this);
        tabImageView = findViewById(R.id.iv_image);
        tabIconView = findViewById(R.id.tv_icon);
        tabNameView = findViewById(R.id.tv_name);
    }

    @Override
    public void setOkTabInfo(@NonNull OkTabBottomInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    /**
     * 填充视图
     * @param selected TabBottom是否选中
     * @param init 标识TabBottom是否是第一次初始化
     */
    private void inflateInfo(boolean selected, boolean init) {
        // IconFont
        if (tabInfo.tabType == OkTabBottomInfo.TabType.ICON) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabIconView.setVisibility(VISIBLE);
                // 设置字体
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tabIconView.setTypeface(typeface);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                tabIconView.setText(TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.defaultIconName : tabInfo.selectedIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.tintColor));
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            }else {
                tabIconView.setText(tabInfo.defaultIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.defaultColor));
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        }else if (tabInfo.tabType == OkTabBottomInfo.TabType.BITMAP) {
            // Bitmap
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabIconView.setVisibility(GONE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                tabImageView.setImageBitmap(tabInfo.selectedBitmap);
            }else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    /**
     * 改变某个tab的高度
     * @param height 高度
     */
    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedChange(int index, @NonNull OkTabBottomInfo<?> prevInfo, @NonNull OkTabBottomInfo<?> nextInfo) {
        if(prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return;
        }
        if(prevInfo == tabInfo) {
            inflateInfo(false, false);
        }else {
            inflateInfo(true, false);
        }
    }

    public OkTabBottomInfo<?> getOkTabBottomInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabIconView() {
        return tabIconView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    /**
     * 将泛型的color转换成int类型的color
     * @param color 颜色（泛型）
     * @return
     */
    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        }else {
            return (int) color;
        }
    }
}
