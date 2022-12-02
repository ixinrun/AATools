package com.ixinrun.lib_aatools.tools.file;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述: 沙盒文件预览
 * </p>
 *
 * @author ixinrun
 * @date 2020/9/27
 */
public class FileViewActivity extends BaseActivity {
    private Toolbar mToolbar;
    private RecyclerView mRv;
    private FileViewAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.file_view_activity;
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FileViewAdapter(null);
        mRv.setAdapter(mAdapter);
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
        List<MultiItemEntity> data = new ArrayList<>();
        // 内部私有存储
        FileFolderItem internalFolder = createFoldItem("内部私有存储", getCacheDir().getParent());
        if (internalFolder != null) {
            data.add(internalFolder);
        }
        // 外部私有存储
        FileFolderItem externalFolder = createFoldItem("外部私有存储", getExternalCacheDir().getParent());
        if (externalFolder != null) {
            data.add(externalFolder);
        }

        // 刷新界面
        mAdapter.setNewData(data);
    }

    /**
     * 创建跟文件夹
     *
     * @param name 名字
     * @param path 路径
     */
    private FileFolderItem createFoldItem(String name, String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        FileFolderItem folder = new FileFolderItem(name, path);
        mapFolder(folder, file);
        return folder;
    }

    /**
     * 文件夹映射
     *
     * @param folder 文件夹
     * @param dir    文件目录
     */
    private void mapFolder(FileFolderItem folder, File dir) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            // 空文件夹
            FileItem fileItem = new FileItem(true);
            folder.addSubItem(fileItem);
        } else {
            // 遍历子文件
            for (File f : files) {
                if (f.isDirectory()) {
                    FileFolderItem childFolder = new FileFolderItem(f.getName(), f.getAbsolutePath());
                    folder.addSubItem(childFolder);
                    // 子文件夹映射
                    mapFolder(childFolder, f);
                } else {
                    FileItem child = new FileItem(f.getName(), f.getAbsolutePath());
                    folder.addSubItem(child);
                }
            }
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FileViewActivity.class);
        context.startActivity(intent);
    }
}
