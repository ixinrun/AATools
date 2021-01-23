package com.ixinrun.aatools

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ixinrun.lib_aatools.AAToolsMgr
import com.ixinrun.lib_aatools.tools.crash_log.CrashHandler
import java.io.File

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AAToolsMgr.getInstance()
                .init(this)
                .initCrashLog(null, 7.0, object : CrashHandler.Listener {
                    override fun onExceptionOccurred(ex: Throwable?, exf: File?): Boolean {
                        Handler(Looper.getMainLooper()).post(object : Runnable {
                            override fun run() {
                                Toast.makeText(this@MyApp, "哦吼，发生异常了", Toast.LENGTH_LONG).show()
                            }
                        })
                        return true
                    }
                })
                .initLocalFile(CrashHandler.getInstance().crashFilesPath)
    }
}