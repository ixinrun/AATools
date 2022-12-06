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

import com.ixinrun.lib_aatools.tools.float_view.DeveloperBall;
import com.ixinrun.lib_aatools.tools.float_view.PageTracker;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Activity resumedAct;
    private DeveloperBall developerBall;
    private PageTracker tracker;

    public Activity getResumedAct() {
        return resumedAct;
    }

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
        resumedAct = activity;
        if (developerBall == null) {
            developerBall = new DeveloperBall(activity);
        }
        developerBall.show();

        if (tracker == null) {
            tracker = new PageTracker(activity);
        }
        tracker.showAt(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        resumedAct = null;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (resumedAct == null) {
                    if (developerBall != null) {
                        developerBall.dismiss();
                    }
                    if (tracker != null) {
                        tracker.dismiss();
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
