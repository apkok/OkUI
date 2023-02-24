package com.jssonok.ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jssonok.ui.R;
import com.jssonok.ui.tab.common.IOkTab;

public class OkTabTop extends RelativeLayout implements IOkTab<OkTabTopInfo<?>> {

    private OkTabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabNameView;
    private View indicator;

    public OkTabTop(Context context) {
        this(context, null);
    }

    public OkTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OkTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 加载视图
     */
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ok_tab_top, this);
        tabImageView = findViewById(R.id.iv_image);
        tabNameView = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setOkTabInfo(@NonNull OkTabTopInfo<?> data) {
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
        if (tabInfo.tabType == OkTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);

                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            }else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        }else if (tabInfo.tabType == OkTabTopInfo.TabType.BITMAP) {
            // Bitmap
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
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
    public void onTabSelectedChange(int index, @NonNull OkTabTopInfo<?> prevInfo, @NonNull OkTabTopInfo<?> nextInfo) {
        if(prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return;
        }
        if(prevInfo == tabInfo) {
            inflateInfo(false, false);
        }else {
            inflateInfo(true, false);
        }
    }

    public OkTabTopInfo<?> getOkTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
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
