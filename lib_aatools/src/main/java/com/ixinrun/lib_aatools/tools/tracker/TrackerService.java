package com.ixinrun.lib_aatools.tools.tracker;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author ixinrun
 * @date 2020/9/24
 */
public class TrackerService extends Service {

    /**
     * 悬浮框管理器
     */
    private TrackerWindowManager mTrackerWindowManager;

    /**
     * activity生命周期监听
     */
    private ActivityLifecycle mLifecycle;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mLifecycle == null) {
            mLifecycle = new ActivityLifecycle();
            getApplication().registerActivityLifecycleCallbacks(mLifecycle);
        }
    }

    class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private boolean isForeground;

        private void onForegroundCallback() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mTrackerWindowManager == null) {
                        return;
                    }
                    if (isForeground) {
                        mTrackerWindowManager.addView();
                    } else {
                        mTrackerWindowManager.removeView();
                    }
                }
            }, 200);
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            isForeground = true;
            onForegroundCallback();
            if (mTrackerWindowManager == null) {
                return;
            }
            CharSequence packageName = activity.getPackageName();
            CharSequence className = activity.getClass().getName();
            Log.e("TAG", "-----------onResumed" + packageName + "-------------" + className);
            if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(className)) {
                String pn = packageName.toString();
                String cn = className.toString();
                if (cn.startsWith(pn)) {
                    cn = cn.substring(pn.length());
                }
                mTrackerWindowManager.updateFloatingView(pn, cn);
            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            isForeground = false;
            onForegroundCallback();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTrackerWindowManager == null) {
            mTrackerWindowManager = new TrackerWindowManager(this);
        }
        mTrackerWindowManager.addView();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLifecycle != null) {
            getApplication().unregisterActivityLifecycleCallbacks(mLifecycle);
        }
        if (mTrackerWindowManager != null) {
            mTrackerWindowManager.removeView();
        }
    }

    /**
     * 启动服务
     */
    public static void start(Context c) {
        c.startService(new Intent(c, TrackerService.class));
    }

    /**
     * 关闭服务
     */
    public static void stop(Context c) {
        c.stopService(new Intent(c, TrackerService.class));
    }
}
