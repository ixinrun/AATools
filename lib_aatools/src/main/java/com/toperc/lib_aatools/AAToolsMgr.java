package com.toperc.lib_aatools;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.toperc.lib_aatools.base.AAToolsActivity;
import com.toperc.lib_aatools.base.ItemBean;
import com.toperc.lib_aatools.base.Util;
import com.toperc.lib_aatools.tools.crash_log.CrashHandler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 功能描述: 开发工具管理类
 * </p>
 *
 * @author ixinrun
 * @data 2020/8/25
 */
public final class AAToolsMgr {
    private static AAToolsMgr INSTANCE = new AAToolsMgr();

    private Application mApp;

    public static AAToolsMgr getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param app
     * @return
     */
    public AAToolsMgr init(Application app) {
        Util.sApp = app;
        this.mApp = app;
        return this;
    }

//    /**
//     * 设置serverlog
//     *
//     * @param ip  serverlog 服务器ip
//     * @param tag 当前设备唯一标识，传空会默认设置标识
//     * @return
//     */
//    public AAToolsMgr initServerLog(String ip, String tag) {
//        return this;
//    }

    /**
     * 设置CrashLog
     *
     * @param savePath 保存路径
     * @param saveDay  保存天数
     * @param l        崩溃回调
     * @return
     */
    public AAToolsMgr initCrashLog(String savePath, double saveDay, CrashHandler.Listener l) {
        CrashHandler.getInstance().init(mApp, savePath, saveDay, l);
        return this;
    }

    /**
     * 应用文件路径
     *
     * @param filePath
     * @return
     */
    public AAToolsMgr initLocalFile(String... filePath) {
        Util.sFilePaths = filePath;
        return this;
    }

    /**
     * 添加自定义工具
     *
     * @param beans
     * @return
     */
    public AAToolsMgr setCustomTools(ItemBean... beans) {
        if (beans != null) {
            Util.sCustomItems = new ArrayList<>();
            Util.sCustomItems.addAll(Arrays.asList(beans));
        }
        return this;
    }

    /**
     * 开启调试助手
     *
     * @param context
     */
    public static void open(Context context) {
        context.startActivity(new Intent(context, AAToolsActivity.class));
    }

}
