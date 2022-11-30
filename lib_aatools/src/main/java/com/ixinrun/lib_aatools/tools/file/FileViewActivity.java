package com.ixinrun.lib_aatools.tools.file;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.BaseActivity;
import com.ixinrun.lib_aatools.base.Util;

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
    private TextView mEmptyView;
    private RecyclerView mFileRv;
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
        mEmptyView = findViewById(R.id.empty_view);
        mFileRv = findViewById(R.id.file_rv);
        mFileRv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FileViewAdapter(null);
        mFileRv.setAdapter(mAdapter);
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
        if (Util.sFilePaths == null) {
            return;
        }
        mEmptyView.setVisibility(View.GONE);

        List<MultiItemEntity> data = new ArrayList<>();
        for (String folderPath : Util.sFilePaths) {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                continue;
            }
            //构建文件夹
            FileFolderItem folderItem = new FileFolderItem(folder.getName(), folder.getAbsolutePath());
            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                //空文件夹
                FileItem fileItem = new FileItem(true);
                folderItem.addSubItem(fileItem);
            } else {
                //遍历子文件
                for (File file : files) {
                    FileItem fileItem = new FileItem(file.getName(), file.getAbsolutePath());
                    folderItem.addSubItem(fileItem);
                }
            }

            //列表追加
            data.add(folderItem);
        }

        //刷新界面
        mAdapter.setNewData(data);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FileViewActivity.class);
        context.startActivity(intent);
    }
}
