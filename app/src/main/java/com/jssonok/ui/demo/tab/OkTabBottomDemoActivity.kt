package com.jssonok.ui.demo.tab

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jssonok.library.util.OkDisplayUtil
import com.jssonok.ui.R
import com.jssonok.ui.tab.bottom.OkTabBottomInfo
import com.jssonok.ui.tab.bottom.OkTabBottomLayout

class OkTabBottomDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ok_tab_bottom_demo)

        initTabBottom()
    }

    private fun initTabBottom() {
        val okTabBottomLayout: OkTabBottomLayout = findViewById(R.id.ok_tab_bottom_layout)
        okTabBottomLayout.setTabBottomAlpha(0.85f)
        val bottomInfoList: MutableList<OkTabBottomInfo<*>> = ArrayList()
        val homeInfo = OkTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val favoriteInfo = OkTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )
        /*val categoryInfo = OkTabBottomInfo(
            "分类",
            "fonts/iconfont.ttf",
            getString(R.string.if_category),
            null,
            "#ff656667",
            "#ffd44949"
        )*/
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)
        val centerInfo = OkTabBottomInfo<String>(
            "分类",
            bitmap,
            bitmap
        )

        val recommendInfo = OkTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val profileInfo = OkTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )

        bottomInfoList.add(homeInfo)
        bottomInfoList.add(favoriteInfo)
        bottomInfoList.add(centerInfo)
        bottomInfoList.add(recommendInfo)
        bottomInfoList.add(profileInfo)

        okTabBottomLayout.inflateInfo(bottomInfoList)
        okTabBottomLayout.addTabSelectedChangeListener{ _, _, nextInfo ->
            Toast.makeText(this@OkTabBottomDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
        }
        okTabBottomLayout.defaultSelected(homeInfo)
        // 改变某个TabBottom的高度
        val tabBottom = okTabBottomLayout.findTab(bottomInfoList[2])
        tabBottom?.apply { resetHeight(OkDisplayUtil.dp2px(66f, resources)) }
    }
}