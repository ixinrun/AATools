package com.example.lib_aatools.tools.tracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

/**
 * @author toperc
 * @data 2020/9/24
 */
public class TrackerUtil {

    /**
     * 判断辅助功能是否打开
     *
     * @param context
     * @return
     */
    private static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

    /**
     * 打开Tracker
     *
     * @param context
     * @return
     */
    public static boolean openTracker(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())));
            Toast.makeText(context, "请先授予 \"Activity 栈\" 悬浮窗权限", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isAccessibilitySettingsOn(context)) {
            context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(context, "请先开启 \"Activity 栈\" 的辅助功能", Toast.LENGTH_LONG).show();
            return false;
        }

        context.startService(new Intent(context, TrackerService.class).putExtra(TrackerService.COMMAND, TrackerService.COMMAND_OPEN));
        return true;
    }
}
