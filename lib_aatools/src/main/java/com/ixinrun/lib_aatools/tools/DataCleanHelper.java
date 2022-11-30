package com.ixinrun.lib_aatools.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 功能描述: 一键清空
 * </p>
 *
 * @author ixinrun
 * @date 2020/9/25
 */
public class DataCleanHelper {

    /**
     * 清除本应用内部cache下的内容(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用内部files下的内容(/data/data/com.xxx.xxx/files)
     */
    public static void cleanInternalFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除本应用外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除本应用外部files下的内容(/mnt/sdcard/android/data/com.xxx.xxx/files)
     */
    public static void cleanExternalFiles(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalFilesDir(null));
        }
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用某个数据库
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 清除本应用所有数据
     *
     * @param context           上下文
     * @param customDirectories 自定义文件夹
     */
    public static void cleanApplicationData(Context context, String... customDirectories) {
        if (context == null) {
            return;
        }
        cleanInternalCache(context);
        cleanInternalFiles(context);
        cleanExternalCache(context);
        cleanExternalFiles(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        if (customDirectories != null) {
            for (String dir : customDirectories) {
                cleanCustomCache(dir);
            }
        }
    }
}
