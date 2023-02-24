package com.jssonok.ui.demo.tab;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jssonok.ui.R;
import com.jssonok.ui.tab.common.IOkTabLayout;
import com.jssonok.ui.tab.top.OkTabTopInfo;
import com.jssonok.ui.tab.top.OkTabTopLayout;

import java.util.ArrayList;
import java.util.List;

public class OkTabTopDemoActivity extends AppCompatActivity {

    String[] tabsStr = new String[]{
            "热门",
            "服装",
            "数码",
            "鞋子",
            "零食",
            "家电",
            "汽车",
            "百货",
            "家居",
            "装修",
            "运动"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ok_tab_top_demo);

        initTabTop();
    }

    private void initTabTop() {
        OkTabTopLayout okTabTopLayout = findViewById(R.id.tab_top_layout);
        List<OkTabTopInfo<?>> infoList = new ArrayList<>();
        int defaultColor = getResources().getColor(R.color.tabTopDefaultColor);
        int tintColor = getResources().getColor(R.color.tabTopTintColor);

        for (String tabStr : tabsStr) {
            OkTabTopInfo<?> info = new OkTabTopInfo<>(tabStr, defaultColor, tintColor);
            infoList.add(info);
        }
        okTabTopLayout.inflateInfo(infoList);
        okTabTopLayout.addTabSelectedChangeListener(new IOkTabLayout.OnTabSelectedListener<OkTabTopInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable OkTabTopInfo<?> prevInfo, @NonNull OkTabTopInfo<?> nextInfo) {
                Toast.makeText(OkTabTopDemoActivity.this, nextInfo.name, Toast.LENGTH_SHORT).show();
            }
        });
        okTabTopLayout.defaultSelected(infoList.get(0));
    }
}
