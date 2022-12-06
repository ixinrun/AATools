package com.ixinrun.lib_aatools.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ActivityLifecycleUtil {
    private List<OnForegroundListener> mListeners = new ArrayList<>();

    private static ActivityLifecycleUtil instance;

    public static ActivityLifecycleUtil getInstance() {
        if (instance == null) {
            instance = new ActivityLifecycleUtil();
        }
        return instance;
    }

    /**
     * 注册ActivityLifecycle
     */
    public ActivityLifecycleUtil register(Application app) {
        app.registerActivityLifecycleCallbacks(new ActivityLifecycle());
        return this;
    }

    /**
     * 设置前台Activity监听
     *
     * @param l
     */
    public void addForegroundListener(OnForegroundListener l) {
        this.mListeners.add(l);
    }

    public interface OnForegroundListener {
        /**
         * 处于前台
         *
         * @param act 前台Activity
         */
        void onForeground(Activity act);

        /**
         * 不可见
         */
        void onDismiss();
    }

    class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private boolean isForeground;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    Toast.makeText(activity, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivity(intent);
                }
            }
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            isForeground = true;
            for (OnForegroundListener l : mListeners) {
                l.onForeground(activity);
            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            isForeground = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isForeground) {
                        for (OnForegroundListener l : mListeners) {
                            l.onDismiss();
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
}
