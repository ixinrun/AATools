package com.ixinrun.lib_aatools.tools.crash_log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.ixinrun.lib_aatools.base.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: Crash捕获
 * </p>
 *
 * @author ixinrun
 * @date 2018/8/10
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Listener mListener;

    /**
     * 系统默认异常处理器，有可能已被友盟或者Bugly给占用
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler INSTANCE;

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    public void init() {
        // 获取默认的UncaughtException处理器
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 日志文件默认保留一周
        autoClear(7.0);
    }

    /**
     * 崩溃回调
     */
    public void setCrashListener(Listener listener) {
        this.mListener = listener;
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        boolean isDefHandler = true;
        if (ex != null) {
            try {
                // 异常存储
                File file = saveCrashInfo2File(Util.sApp, ex);
                // 异常外抛
                if (mListener != null) {
                    isDefHandler = mListener.onExceptionOccurred(ex, file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 调用默认的异常处理器处理异常
        if (isDefHandler && mDefaultHandler != null && mDefaultHandler != this) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 保存日志文件
     *
     * @param ex
     * @return 日志内容
     */
    private File saveCrashInfo2File(Context context, Throwable ex) {
        StringBuilder sb = new StringBuilder();

        // 设备信息
        Map<String, String> info = collectDeviceInfo(context);
        if (info != null && !info.isEmpty()) {
            for (Map.Entry<String, String> entry : info.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("\r\n");
            }
            // 分割
            sb.append("\r\n");
        }

        // 栈信息
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        // 保存文件
        String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String fileName = time + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            try {
                File dir = new File(getCrashFilesPath());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, fileName);
                fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    private Map<String, String> collectDeviceInfo(Context context) {
        Map<String, String> info = new HashMap<>();
        // 获取应用信息
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 反射机制获取设备信息。
        // 获取设备信息类中的所有变量信息。
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // 对私有对象进行访问
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return info;
    }

    /**
     * 获取日志存储文件夹
     *
     * @return
     */
    public String getCrashFilesPath() {
        return Util.sApp.getExternalFilesDir("logs") + File.separator;
    }

    /**
     * 定期删除日志文件
     *
     * @param day 文件保存天数
     */
    private void autoClear(double day) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        long now = System.currentTimeMillis();
        File file = new File(getCrashFilesPath());
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (now - files[i].lastModified() > day * 24 * 60 * 60 * 1000) {
                        files[i].delete();
                    }
                }
            }
        }
    }

    public interface Listener {

        /**
         * 发生异常（非主线程）
         *
         * @param ex
         * @param exf
         * @return 否由默认异常处理器处理异常
         */
        boolean onExceptionOccurred(Throwable ex, File exf);
    }
}
