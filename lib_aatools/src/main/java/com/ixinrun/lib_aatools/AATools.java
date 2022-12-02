package com.ixinrun.lib_aatools;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.ixinrun.lib_aatools.base.AAToolsActivity;
import com.ixinrun.lib_aatools.base.ItemBean;
import com.ixinrun.lib_aatools.base.Util;
import com.ixinrun.lib_aatools.tools.crash_log.CrashHandler;

import java.util.Arrays;

/**
 * 功能描述: 开发工具管理类
 * </p>
 *
 * @author ixinrun
 * @date 2020/8/25
 */
public final class AATools {
    /**
     * 初始配置
     */
    public static Builder with(Application app) {
        return new Builder(app);
    }

    public static class Builder {

        public Builder(Application app) {
            Util.sApp = app;
        }

        /**
         * 崩溃日志设置
         *
         * @param savePath 存储路径
         * @param saveDay  存储天数
         * @param l        崩溃回调
         */
        public Builder setCrashLog(String savePath, double saveDay, CrashHandler.Listener l) {
            CrashHandler.getInstance().init(Util.sApp, savePath, saveDay, l);
            return this;
        }

        /**
         * 其他文件夹路径，如果是单个文件不生效
         *
         * @param dirs 文件夹路径
         */
        public Builder setOtherDirs(String... dirs) {
            Util.sOtherDirs = dirs;
            return this;
        }

        /**
         * 自定义小工具
         *
         * @param beans 小工具构造实体
         */
        public Builder setCustomTools(ItemBean... beans) {
            if (beans != null) {
                Util.sCustomItems.addAll(Arrays.asList(beans));
            }
            return this;
        }
    }

    /**
     * 开启调试助手
     */
    public static void open(Context context) {
        context.startActivity(new Intent(context, AAToolsActivity.class));
    }

}
