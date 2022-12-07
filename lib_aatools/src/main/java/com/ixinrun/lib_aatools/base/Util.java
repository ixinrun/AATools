package com.ixinrun.lib_aatools.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述: 工具配置项
 * </p>
 *
 * @author ixinrun
 * @date 2020/9/25
 */
public class Util {

    public static Application sApp;

    public static String[] sOtherDirs;

    public static List<ItemBean> sCustomItems = new ArrayList<>();

    public static boolean hasOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Toast.makeText(context, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
                return false;
            }
        }
        return true;
    }
}
