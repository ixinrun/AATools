package com.ixinrun.lib_aatools.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ixinrun.lib_aatools.tools.tracker.TrackerWindowManager;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Activity resumedAct;
    private TrackerWindowManager mTrackerWindowManager;

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

    private void showTracker() {
        Activity a = getResumedAct();
        if (a == null) {
            return;
        }
        if (mTrackerWindowManager == null) {
            mTrackerWindowManager = new TrackerWindowManager(a);
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
}
