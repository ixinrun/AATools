package com.ixinrun.lib_aatools.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityLifecycleUtil {
    private final List<OnForegroundListener> mListeners = new ArrayList<>();
    private final List<ActivityLog> mActivityLogs = new ArrayList<>();

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

    /**
     * 获取activity生命周期日志
     */
    public List<ActivityLog> getActivityLogs() {
        return mActivityLogs;
    }

    public static class ActivityLog {
        public String name;
        public String tag;
        public Date time;

        public ActivityLog(String name, String tag) {
            this.name = name;
            this.tag = tag;
            this.time = new Date();
        }
    }

    class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private boolean isForeground;

        private void log(Activity activity, String tag) {
            mActivityLogs.add(0, new ActivityLog(activity.getClass().getName(), tag));
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            log(activity, "onActivityCreated");
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            log(activity, "onActivityStarted");
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            log(activity, "onActivityResumed");
            isForeground = true;
            for (OnForegroundListener l : mListeners) {
                l.onForeground(activity);
            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            log(activity, "onActivityPaused");
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
            log(activity, "onActivityStopped");
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            log(activity, "onActivitySaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            log(activity, "onActivityDestroyed");
            if (mActivityLogs.size() > 500) {
                mActivityLogs.subList(0, mActivityLogs.size() - 500).clear();
            }
        }
    }
}
