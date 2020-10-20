package com.example.lib_aatools.tools.tracker;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author toperc
 * @data 2020/9/24
 */
public class TrackerService extends AccessibilityService {
    public static final String TAG = "TrackerService";
    public static final String COMMAND = "COMMAND";
    public static final String COMMAND_OPEN = "COMMAND_OPEN";
    public static final String COMMAND_CLOSE = "COMMAND_CLOSE";
    TrackerWindowManager mTrackerWindowManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initTrackerWindowManager() {
        if (mTrackerWindowManager == null) {
            mTrackerWindowManager = new TrackerWindowManager(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTrackerWindowManager();

        String command = intent.getStringExtra(COMMAND);
        if (command != null) {
            if (command.equals(COMMAND_OPEN)) {
                mTrackerWindowManager.addView();
            } else if (command.equals(COMMAND_CLOSE)) {
                mTrackerWindowManager.removeView();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            CharSequence packageName = event.getPackageName();
            CharSequence className = event.getClassName();
            if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(className)) {
                String pn = packageName.toString();
                String cn = className.toString();
                if (cn.startsWith(pn)) {
                    cn = cn.substring(pn.length());
                }

                if (mTrackerWindowManager != null) {
                    mTrackerWindowManager.updateFloatingView(pn, cn);
                }
            }
        }
    }
}
