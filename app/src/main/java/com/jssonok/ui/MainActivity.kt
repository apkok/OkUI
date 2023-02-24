package com.jssonok.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jssonok.ui.demo.tab.OkTabBottomDemoActivity
import com.jssonok.ui.demo.tab.OkTabTopDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.tv_tab_bottom -> {
                startActivity(Intent(this, OkTabBottomDemoActivity::class.java))
            }
            R.id.tv_tab_top -> {
                startActivity(Intent(this, OkTabTopDemoActivity::class.java))
            }
        }
    }
}