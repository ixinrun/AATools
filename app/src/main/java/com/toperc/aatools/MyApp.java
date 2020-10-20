package com.toperc.aatools;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.toperc.lib_aatools.AAToolsMgr;
import com.toperc.lib_aatools.tools.crash_log.CrashHandler;

import java.io.File;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AAToolsMgr.getInstance()
                .init(this)
                .initCrashLog(null, 7, new CrashHandler.Listener() {
                    @Override
                    public boolean onExceptionOccurred(Throwable ex, File exf) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApp.this, "哦吼，发生异常了", Toast.LENGTH_LONG).show();
                            }
                        });
                        return true;
                    }
                })
                .initLocalFile(CrashHandler.getInstance().getCrashFilesPath());
    }
}
