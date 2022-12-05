package com.ixinrun.lib_aatools.tools.tracker;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

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
        private Activity resumedAct;

        public Activity getResumedAct() {
            return resumedAct;
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            resumedAct = activity;
            showTracker();
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            resumedAct = null;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (resumedAct == null) {
                        if (mTrackerWindowManager != null) {
                            mTrackerWindowManager.removeView();
                        }
                    }
                }
            }, 200);
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

    private void showTracker() {
        Activity a = mLifecycle.getResumedAct();
        if (a == null) {
            return;
        }
        if (mTrackerWindowManager == null) {
            mTrackerWindowManager = new TrackerWindowManager(this);
        }
        mTrackerWindowManager.addView();
        CharSequence packageName = a.getPackageName();
        CharSequence className = a.getClass().getName();
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        showTracker();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(c)) {
                Toast.makeText(c, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + c.getPackageName()));
                c.startActivity(intent);
            } else {
                c.startService(new Intent(c, TrackerService.class));
            }
        }
    }

    /**
     * 关闭服务
     */
    public static void stop(Context c) {
        c.stopService(new Intent(c, TrackerService.class));
    }
}
