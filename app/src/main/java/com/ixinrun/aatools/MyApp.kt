package com.ixinrun.aatools

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ixinrun.lib_aatools.AATools
import com.ixinrun.lib_aatools.base.ItemBean
import com.ixinrun.lib_aatools.tools.crash_log.CrashHandler
import java.io.File

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AATools.with(this)
            .setCrashLog(null, 7.0, object : CrashHandler.Listener {
                override fun onExceptionOccurred(ex: Throwable?, exf: File?): Boolean {
                    Handler(Looper.getMainLooper()).post(object : Runnable {
                        override fun run() {
                            Toast.makeText(
                                this@MyApp,
                                "哦吼，发生异常了，赶快上传服务器：\n" + ex.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                    return true
                }
            })
            .setCustomTools(
                ItemBean(
                    R.drawable.ic_launcher_background,
                    "我的应用1",
                    object : ItemBean.OnItemClickListener {
                        override fun onClick(context: Context?): Boolean {
                            Toast.makeText(this@MyApp, "点击了我的应用1", Toast.LENGTH_LONG).show()
                            return false
                        }
                    }
                ),
                ItemBean(
                    R.drawable.ic_launcher_background,
                    "我的应用2",
                    object : ItemBean.OnItemClickListener {
                        override fun onClick(context: Context?): Boolean {
                            Toast.makeText(this@MyApp, "点击了我的应用2", Toast.LENGTH_LONG).show()
                            return false
                        }
                    }
                )
            )
    }
}