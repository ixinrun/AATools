package com.ixinrun.aatools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.ixinrun.lib_aatools.AAToolsMgr

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.open_btn)
                .setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        AAToolsMgr.open(this@MainActivity)
                    }
                })

        findViewById<View>(R.id.test_crash_btn)
                .setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val a = 1 / 0
                    }
                })
    }
}