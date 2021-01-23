package com.ixinrun.lib_aatools.tools.crash_log;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 功能描述: 日志展示页面
 * </p>
 *
 * @author ixinrun
 * @data 2020/9/24
 */
public class CrashLogActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TextView mEmptyView;
    private TextView mLogContentTv;

    @Override
    protected int getContentView() {
        return R.layout.crash_log_activity;
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEmptyView = findViewById(R.id.empty_view);
        mLogContentTv = findViewById(R.id.log_content_tv);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        File file = new File(CrashHandler.getInstance().getCrashFilesPath());
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                File lastLog = files[files.length - 1];
                String name = lastLog.getName();
                String log = readFromSD(lastLog.getAbsolutePath());
                mToolbar.setSubtitle(name);
                mLogContentTv.setText(log);
            }
        }

        if (TextUtils.isEmpty(mLogContentTv.getText())) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 从SD卡中读取文件
     */
    private String readFromSD(String filePath) {
        String content = "";
        ByteArrayOutputStream ops = null;
        FileInputStream fis = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File file = new File(filePath);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    ops = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] data = new byte[1024];
                    while ((len = fis.read(data)) != -1) {
                        ops.write(data, 0, len);
                    }
                    content = new String(ops.toByteArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ops != null) {
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CrashLogActivity.class);
        context.startActivity(intent);
    }
}
